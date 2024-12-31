package srpmultiplier.mixin.lostcitytweaks.bloodmoon;

import lumien.bloodmoon.server.BloodmoonHandler;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
    private static int increaseParasiteMobCap() {
        if (SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension == 111)
            return SRPConfig.worldMobCap * SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier;
        return SRPConfig.worldMobCap;
    }

    @Redirect(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private static int increaseParasiteMobCapPerPlayer() {
        if (SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension == 111)
            return SRPConfig.worldMobCapPlusPlayer * SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier;
        return SRPConfig.worldMobCapPlusPlayer;
    }
}