package srpmixins.mixin.modcompat.lostcitytweaks.bloodmoon;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {

    @Unique private static Float srpmixins$currentMulti = null;

    @Inject(
            method = "onSpawn",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void srpmixins_overwriteMobCapMultiplier(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci) {
        if (!DimensionMultiConfigProvider.dimensionMobCapMultipliers.containsKey(111)) return;
        int dimension = event.getWorld().provider.getDimension();
        if (SRPMixinsConfigHandler.modcompat.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension == 111) {
            srpmixins$currentMulti = DimensionMultiConfigProvider.dimensionMobCapMultipliers.get(111);
            DimensionMultiConfigProvider.dimensionMobCapMultipliers.put(111, srpmixins$currentMulti * SRPMixinsConfigHandler.modcompat.bloodmoonInLCmobCapMultiplier);
        }
    }

    @Inject(
            method = "onSpawn",
            at = @At(value = "RETURN"),
            remap = false
    )
    private static void srpmixins_resetMobCapMultiplier(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci) {
        if (srpmixins$currentMulti == null) return;
        if (!DimensionMultiConfigProvider.dimensionMobCapMultipliers.containsKey(111)) return;
        int dimension = event.getWorld().provider.getDimension();
        if (SRPMixinsConfigHandler.modcompat.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension == 111) {
            DimensionMultiConfigProvider.dimensionMobCapMultipliers.put(111, srpmixins$currentMulti);
            srpmixins$currentMulti = null;
        }
    }
}