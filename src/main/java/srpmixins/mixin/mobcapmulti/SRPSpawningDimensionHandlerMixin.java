package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {

    @Unique private static int srpmixins$dimension;

    @Inject(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static void srpmixins_saveDimension(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci) {
        srpmixins$dimension = event.getWorld().provider.getDimension();
    }

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static int srpmixins_increaseParasiteMobCap_checkNearby(int original) {
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(srpmixins$dimension,1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private static int srpmixins_increaseParasiteMobCapPerPlayer_checkNearby(int original) {
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(srpmixins$dimension,1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }
}