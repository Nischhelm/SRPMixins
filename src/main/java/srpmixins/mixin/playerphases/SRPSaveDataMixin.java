package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMovingSound;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import srpmixins.SRPMixins;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(SRPSaveData.class)
public abstract class SRPSaveDataMixin implements SRPSaveDataInterface {
    @ModifyArg(
            method = "<init>()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/WorldSavedData;<init>(Ljava/lang/String;)V"),
            remap = false
    )
    private static String srpmixins_changeDataName(String name){
        return name+ srpmixins$uuidtmp;
    }

    @Unique private static String srpmixins$uuidtmp = "";
    @Unique private UUID srpmixins$playerUUID;
    @Override
    public void srpmixins$setUUID(UUID uuid){
        srpmixins$playerUUID = uuid;
    }

    @Unique
    private static SRPSaveData srpmixins$createForPlayer(World world, UUID playerUUID, MapStorage storage) {
        SRPMixins.LOGGER.info("Creating new SRPSaveData for dim{} and player{}", world.provider.getDimension(),playerUUID.toString());
        srpmixins$uuidtmp = playerUUID.toString();
        SRPSaveData instance = new SRPSaveData();
        srpmixins$uuidtmp = "";
        storage.setData("srparasites_global_data" + playerUUID, instance);

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
        instance.markDirty();
    }

    @Override
    public SRPSaveData srpmixins$getByPlayer(World world, UUID playerUUID) {
        if(playerUUID!=null) {
            MapStorage storage = world.getPerWorldStorage();
            SRPSaveData instancePlayer = (SRPSaveData) storage.getOrLoadData(SRPSaveData.class, "srparasites_global_data" + playerUUID);
            if (instancePlayer == null) {
                instancePlayer = srpmixins$createForPlayer(world,playerUUID, storage);
            }
            ((SRPSaveDataInterface) instancePlayer).srpmixins$setUUID(playerUUID);
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

    @WrapOperation(
            method = "checkKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerDim(Lnet/minecraft/world/World;Ljava/lang/String;I)V"),
            remap = false
    )
    private void srpmixins_sendWarningToOnePlayer(World world, String message, int warning, Operation<Void> original){
        if(SRPMixinsConfigHandler.playerphases.enabled && this.srpmixins$playerUUID !=null)
            srpmixins$alertOnePlayer(world,this.srpmixins$playerUUID, message, warning);
        else
            original.call(world, message, warning);
    }

    @WrapOperation(
            method = "checkForUnlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerSer(Ljava/lang/String;Lnet/minecraft/world/World;)V"),
            remap = false
    )
    private void srpmixins_sendParaUnlockMessageToOnePlayer(String message, World world, Operation<Void> original) {
        if(SRPMixinsConfigHandler.playerphases.enabled) {
            EntityPlayer player = world.getPlayerEntityByUUID(srpmixins$playerUUID);
            if(player != null) player.sendMessage(new TextComponentString(message));
        } else
            original.call(message, world);
    }

    @Unique
    private static void srpmixins$alertOnePlayer(World worldIn, UUID playerUUID, String message, int warning) {
        EntityPlayer player = worldIn.getPlayerEntityByUUID(playerUUID);
        if (player == null) return;
        player.sendMessage(new TextComponentString(message));
        SRPMain.network.sendTo(new SRPPacketMovingSound(warning), (EntityPlayerMP) player);

        if (warning == -7 && message.equals("Phase decreased"))
            for (EntityParasiteBase entity : worldIn.getEntities(EntityParasiteBase.class, ent -> ent.getDistanceSq(player) <= 65536))
                entity.addPotionEffect(new PotionEffect(SRPPotions.RAGE_E, 2400, 1, false, false));
    }
}