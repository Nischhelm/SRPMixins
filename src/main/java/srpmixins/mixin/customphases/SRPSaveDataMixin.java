package srpmixins.mixin.customphases;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.SRPMixins;
import srpmixins.SRPMixinsPlugin;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.network.PhaseWarningOverhaul;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;
import srpmixins.util.customphasemechanics.SRPSaveDataPlayerLegacyPatch;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(SRPSaveData.class)
public abstract class SRPSaveDataMixin implements SRPSaveDataInterface {
    @Unique private UUID srpmixins$playerUUID;
    public void srpmixins$setUUID(UUID uuid){
        srpmixins$playerUUID = uuid;
    }
    public UUID srpmixins$getUUID(){
        return srpmixins$playerUUID;
    }

    @Unique
    private static SRPSaveData srpmixins$createForPlayer(World world, UUID playerUUID, MapStorage storage) {
        SRPMixins.LOGGER.info("Creating new SRPSaveData for dim{} and player{}", world.provider.getDimension(),playerUUID.toString());

        String dataName = "srparasites_global_data" + playerUUID;
        SRPSaveData instance = new SRPSaveData(dataName);
        storage.setData(dataName, instance);

        instance.getLockedList().addAll(SRPConfigProvider.lockedParasites);

        for (Map.Entry<Integer, List<Integer>> entry : SRPConfigProvider.evolutionStartPerDimension.entrySet()) {
            int dim = entry.getKey();
            int phase = entry.getValue().get(0);
            int points =  entry.getValue().get(1);

            srpmixins$addDim(instance, dim);
            instance.setEvolutionPhase(dim, (byte) phase, true, world, true);
            if (phase == -2) {
                instance.setGaining(false, dim);
                instance.setLoss(true, dim);
            } else if (phase == -1)
                instance.setTotalKills(dim, -points, false, world, true);
            else
                instance.setTotalKills(dim, points, false, world, true);
        }

        for (int dim : ((SRPSaveDataAccessor) instance).getDimEPid()) {
            boolean dimIsInList = SRPConfigProvider.dimensionCanGainPointsBlacklist.contains(dim);
            if (!SRPConfigSystems.evolutionDimGainInverted == dimIsInList)
                instance.setGaining(false, dim);

            dimIsInList = SRPConfigProvider.dimensionCantLosePointsBlacklist.contains(dim);
            if (!SRPConfigSystems.evolutionDimLossInverted == dimIsInList)
                instance.setLoss(true, dim);
        }

        instance.markDirty();
        return instance;
    }

    @Unique
    private static void srpmixins$addDim(SRPSaveData instance, int id) {
        if(((SRPSaveDataAccessor) instance).getDimEPid().contains(id)) return;

        ((SRPSaveDataAccessor) instance).getDimEPid().add(id);
        ((SRPSaveDataAccessor) instance).getDimEPcanGainPoints().add(true);
        ((SRPSaveDataAccessor) instance).getDimEPcanLossPoints().add(false);
        ((SRPSaveDataAccessor) instance).getDimEPevolution().add(SRPConfigSystems.defaultEvoPhase);
        ((SRPSaveDataAccessor) instance).getDimEPtimeEvolution().add(0);
        ((SRPSaveDataAccessor) instance).getDimEPtotalKills().add(SRPConfigSystems.defaultEvoPoints);
    }

    @Override
    public SRPSaveData srpmixins$getByPlayer(World world, UUID playerUUID) {
        if(playerUUID != null) {
            MapStorage storage = world.getMapStorage();
            SRPSaveData instancePlayer = (SRPSaveData) storage.getOrLoadData(SRPSaveData.class, "srparasites_global_data" + playerUUID);
            if (instancePlayer == null) instancePlayer = srpmixins$createForPlayer(world, playerUUID, storage);

            //legacy garbage
            srpmixins$patchIfAvailable(world, playerUUID, instancePlayer);

            ((SRPSaveDataInterface) instancePlayer).srpmixins$setUUID(playerUUID); //idk why this is here but it shouldnt be an issue
            return instancePlayer;
        }
        if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode)
            SRPMixins.LOGGER.warn("SRPMixins Debug Mode: getByPlayer didnt find player");
        return SRPSaveData.get(world);
    }

    @Override
    public SRPSaveData srpmixins$getByBlock(World world, BlockPos blockPos) {
        if(!world.playerEntities.isEmpty()) {
            if (blockPos != null) {    //TODO: are spawning mobs always at null for parasitetask?
                int x = blockPos.getX();
                int y = blockPos.getY();
                int z = blockPos.getZ();

                EntityPlayer player = srpmixins$getClosestPlayer(world, x, y, z, 256);

                if (player != null)
                    return srpmixins$getByPlayer(world, player.getUniqueID());
                else if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode)
                    SRPMixins.LOGGER.warn("SRPMixins Debug Mode: getByBlock didnt find player {}", blockPos);
            } else if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode)
                SRPMixins.LOGGER.warn("SRPMixins Debug Mode: getByBlock didnt find blockpos");
            if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode) {
                try {
                    throw (new Exception("SRPMixins Debug Mode - Stack Trace"));
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return SRPSaveData.get(world);
    }

    @Unique
    public EntityPlayer srpmixins$getClosestPlayer(World world, double x, double y, double z, double maxDist) {
        double minDist = -1.0D;
        EntityPlayer closestPlayer = null;

        for (EntityPlayer player: world.playerEntities){
            if (!player.isSpectator()){
                double currDistXZ = srpmixins$distSq(player,x,z);
                double currDist = player.getDistanceSq(x,y,z);

                if ((maxDist < 0.0D || currDistXZ < maxDist*maxDist) && (minDist == -1.0D || currDist < minDist)){
                    minDist = currDist;
                    closestPlayer = player;
                }
            }
        }

        return closestPlayer;
    }

    @Unique
    private double srpmixins$distSq(EntityPlayer player, double x, double z) {
        double xD = Math.abs(player.posX-x);
        double zD = Math.abs(player.posZ-z);
        return Math.max(xD,zD);
    }

    @WrapWithCondition(
            method = "checkKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerDim(Lnet/minecraft/world/World;Ljava/lang/String;I)V"),
            remap = false
    )
    private boolean srpmixins_sendWarningToOnePlayer(World world, String message, int warning){
        PhaseWarningOverhaul.alertOnePlayer(world,this.srpmixins$playerUUID, message, warning);
        return false;
    }

    @WrapWithCondition(
            method = "checkForUnlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerSer(Ljava/lang/String;Lnet/minecraft/world/World;)V"),
            remap = false
    )
    private boolean srpmixins_sendParaUnlockMessageToOnePlayer(String message, World world, @Local(name = "id") int paraId) {
        PhaseWarningOverhaul.sendUnlockMessage(world, this.srpmixins$getUUID(), message, paraId);
        return false;
    }

    // ---------------- LEGACY GARBAGE -----------------------
    @Unique private static final String srpmixins$legacyDontReadKey = "srpmixins_legacydata_dont_read";
    @Unique private boolean srpmixins$isLegacyPerDimSaveData = false;
    @Inject(method = "readFromNBT", at = @At("TAIL"))
    private void srpmixins_readLegacyTag(NBTTagCompound compound, CallbackInfo ci){
        if(compound.hasKey(srpmixins$legacyDontReadKey)) this.srpmixins$isLegacyPerDimSaveData = true;
    }
    @Inject(method = "writeToNBT", at = @At("TAIL"))
    private void srpmixins_writeLegacyTag(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir){
        if(this.srpmixins$isLegacyPerDimSaveData) compound.setBoolean(srpmixins$legacyDontReadKey, true);
    }
    public boolean srpmixins$getIsLegacy(){
        return this.srpmixins$isLegacyPerDimSaveData;
    }
    public void srpmixins$setIsLegacy(){
        this.srpmixins$isLegacyPerDimSaveData = true;
    }
    @Unique
    private static void srpmixins$patchIfAvailable(World world, UUID playerUUID, SRPSaveData actualData){
        int dimId = world.provider.getDimension();
        if(SRPMixinsConfigHandler.playerphases.playerPhaseLegacyPatch && dimId != 0){
            MapStorage storagePerWorld = world.getPerWorldStorage();
            SRPSaveData instancePlayerLegacy = (SRPSaveData) storagePerWorld.getOrLoadData(SRPSaveData.class, "srparasites_global_data" + playerUUID);
            if(instancePlayerLegacy != null && !((SRPSaveDataInterface) instancePlayerLegacy).srpmixins$getIsLegacy()) {
                ((SRPSaveDataInterface) instancePlayerLegacy).srpmixins$setIsLegacy(); //Don't read this data ever again after this
                instancePlayerLegacy.markDirty();

                // we gotta get the saved data just from that one dimension
                // and then write it into the actual data object
                new SRPSaveDataPlayerLegacyPatch(dimId, (SRPSaveDataAccessor) instancePlayerLegacy).patchWithLegacyData((SRPSaveDataAccessor) actualData);

                actualData.markDirty();
            }
        }
    }
}