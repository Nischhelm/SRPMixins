package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static int srpmixins_increaseParasiteMobCap_onSpawn(int original, @Local(argsOnly = true) LivingSpawnEvent.CheckSpawn event) {
        float dimensionMultiplier = DimensionMultiConfigProvider.dimensionMobCapMultipliers.getOrDefault(event.getWorld().provider.getDimension(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private static int srpmixins_increaseParasiteMobCapPerPlayer_onSpawn(int original, @Local(argsOnly = true) LivingSpawnEvent.CheckSpawn event) {
        float dimensionMultiplier = DimensionMultiConfigProvider.dimensionMobCapMultipliers.getOrDefault(event.getWorld().provider.getDimension(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }
}