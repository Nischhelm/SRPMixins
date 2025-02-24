package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.EntityPlayer;
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
import srpmixins.util.PlayerPhases_AlertOnePlayer;
import srpmixins.util.SRPSaveDataInterface;

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
    private static String changeDataName(String name){
        return name+uuidtmp;
    }

    @Unique private static String uuidtmp = "";
    @Unique private UUID playerUUID;
    @Override
    public void setUUID(UUID uuid){
        playerUUID = uuid;
    }

    @Unique
    private static SRPSaveData createForPlayer(World world, UUID playerUUID, MapStorage storage) {
        SRPMixins.LOGGER.info("Creating new SRPSaveData for dim{} and player{}", world.provider.getDimension(),playerUUID.toString());
        uuidtmp = playerUUID.toString();
        SRPSaveData instance = new SRPSaveData();
        uuidtmp = "";
        storage.setData("srparasites_global_data" + playerUUID, instance);

        instance.getLockedList().addAll(SRPConfigProvider.lockedParasites);

        for (Map.Entry<Integer, List<Integer>> entry : SRPConfigProvider.evolutionStartPerDimension.entrySet()) {
            int dim = entry.getKey();
            int phase = entry.getValue().get(0);
            int points =  entry.getValue().get(1);

            addDim(instance, dim);
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
    private static void addDim(SRPSaveData instance, int id) {
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
    public SRPSaveData getByPlayer(World world, UUID playerUUID) {
        if(playerUUID!=null) {
            MapStorage storage = world.getPerWorldStorage();
            SRPSaveData instancePlayer = (SRPSaveData) storage.getOrLoadData(SRPSaveData.class, "srparasites_global_data" + playerUUID);
            if (instancePlayer == null) {
                instancePlayer = createForPlayer(world,playerUUID, storage);
            }
            ((SRPSaveDataInterface) instancePlayer).setUUID(playerUUID);
            return instancePlayer;
        }
        if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode)
            SRPMixins.LOGGER.info("SRPMixins Debug Mode: getByPlayer didnt find player");
        return SRPSaveData.get(world);
    }

    @Override
    public SRPSaveData getByBlock(World world, BlockPos blockPos) {
        if(!world.playerEntities.isEmpty()) {
            if (blockPos != null) {    //TODO: are spawning mobs always at null for parasitetask?
                int x = blockPos.getX();
                int y = blockPos.getY();
                int z = blockPos.getZ();

                EntityPlayer player = getClosestPlayer(world, x, y, z, 256);

                if (player != null)
                    return getByPlayer(world, player.getUniqueID());
                else if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode)
                    SRPMixins.LOGGER.info("SRPMixins Debug Mode: getByBlock didnt find player {}", blockPos);
            } else if(SRPMixinsConfigHandler.playerphases.playerPhaseDebugMode)
                SRPMixins.LOGGER.info("SRPMixins Debug Mode: getByBlock didnt find blockpos");
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
    public EntityPlayer getClosestPlayer(World world, double x, double y, double z, double maxDist) {
        double minDist = -1.0D;
        EntityPlayer closestPlayer = null;

        for (EntityPlayer player: world.playerEntities){
            if (!player.isSpectator()){
                double currDistXZ = distSq(player,x,z);
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
    private double distSq(EntityPlayer player, double x, double z) {
        double xD = Math.abs(player.posX-x);
        double zD = Math.abs(player.posZ-z);
        return Math.max(xD,zD);
    }

    @WrapOperation(
            method = "checkKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerDim(Lnet/minecraft/world/World;Ljava/lang/String;I)V"),
            remap = false
    )
    private void sendWarningToOnePlayer(World world, String message, int warning, Operation<Void> original){
        if(SRPMixinsConfigHandler.playerphases.enabled && this.playerUUID!=null)
            PlayerPhases_AlertOnePlayer.alertOnePlayer(world,this.playerUUID, message, warning);
        else
            original.call(world, message, warning);
    }

    @WrapOperation(
            method = "checkForUnlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerSer(Ljava/lang/String;Lnet/minecraft/world/World;)V"),
            remap = false
    )
    private void sendParaUnlockMessageToOnePlayer(String message, World world, Operation<Void> original){
        if(SRPMixinsConfigHandler.playerphases.enabled) {
            EntityPlayer player = world.getPlayerEntityByUUID(playerUUID);
            if(player != null) player.sendMessage(new TextComponentString(message));
        } else
            original.call(message, world);
    }
}