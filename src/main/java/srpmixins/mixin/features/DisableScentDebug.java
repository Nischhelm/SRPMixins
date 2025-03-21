package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.PrintStream;

@Mixin(EntityParasiticScent.class)
public abstract class DisableScentDebug {
    @WrapWithCondition(
            method = "onCollideWithPlayer",
            at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V")
    )
    private boolean srpmixins_disableScentCollideDebug(PrintStream instance, String x) {
        //no op if disabled
        return false;
    }

    @WrapWithCondition(
            method = "placeWaves",
            at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"),
            remap = false
    )
    private boolean srpmixins_disableScentHitboxDebug(PrintStream instance, String x){
        //no op if disabled
        return false;
    }
}