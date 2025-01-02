package srpmultiplier.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {

    @Unique private static int dimension;

    @Inject(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static void saveDimension(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci) {
        dimension = event.getWorld().provider.getDimension();
    }

    @Redirect(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static int increaseParasiteMobCap_checkNearby() {
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.server.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCap * dimensionMultiplier);
        return SRPConfig.worldMobCap;
    }

    @Redirect(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private static int increaseParasiteMobCapPerPlayer_checkNearby() {
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.server.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCapPlusPlayer * dimensionMultiplier);
        return SRPConfig.worldMobCapPlusPlayer;
    }
}