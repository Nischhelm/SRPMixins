package srpmultiplier.mixin.lostcitytweaks.bloodmoon;

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
import srpmultiplier.handlers.SRPMultiplierConfigHandler;


@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPWorldEntitySpawnerMixin {

    @Unique
    private static int dimension;

    @Inject(
            method="onSpawn",
            at= @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static void saveDimensionMixin(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci){
        dimension = event.getWorld().provider.getDimension();
    }

    @Redirect(
            method="onSpawn",
            at=@At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap=false
    )
    private static int increaseParasiteMobCapMixin(){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension==111)
            return SRPConfig.worldMobCap* SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier;
        return SRPConfig.worldMobCap;
    }

    @Redirect(
            method="onSpawn",
            at=@At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap=false
    )
    private static int increaseParasiteMobCap2Mixin(){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension==111)
            return SRPConfig.worldMobCapPlusPlayer* SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier;
        return SRPConfig.worldMobCapPlusPlayer;
    }
}