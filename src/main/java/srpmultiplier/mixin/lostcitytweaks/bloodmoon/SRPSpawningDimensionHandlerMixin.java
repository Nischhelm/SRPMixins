package srpmultiplier.mixin.lostcitytweaks.bloodmoon;

import lumien.bloodmoon.server.BloodmoonHandler;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {

    @Unique
    private static Float currentMulti = null;

    @Inject(
            method = "onSpawn",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void overwriteMobCapMultiplier(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci) {
        if (!SRPMultiplier.dimensionMobCapMultipliers.containsKey(111)) return;
        int dimension = event.getWorld().provider.getDimension();
        if (SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension == 111) {
            currentMulti = SRPMultiplier.dimensionMobCapMultipliers.get(111);
            SRPMultiplier.dimensionMobCapMultipliers.put(111, currentMulti * SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier);
        }
    }

    @Inject(
            method = "onSpawn",
            at = @At(value = "RETURN"),
            remap = false
    )
    private static void resetMobCapMultiplier(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci) {
        if (currentMulti == null) return;
        if (!SRPMultiplier.dimensionMobCapMultipliers.containsKey(111)) return;
        int dimension = event.getWorld().provider.getDimension();
        if (SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension == 111) {
            SRPMultiplier.dimensionMobCapMultipliers.put(111, currentMulti);
            currentMulti = null;
        }
    }
}