package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParasiteEventWorld.class)
public abstract class BeckonInfestationLimit {
    @ModifyExpressionValue(
            method = "canInfestBlock",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigMobs;venkrolsiiiRange:I"),
            remap = false
    )
    private static int srpmixins_limitInfestation(int original, @Local(argsOnly = true) int stage) {
        switch (stage) {
            case 1: return SRPConfigMobs.venkrolRange;
            case 2: return SRPConfigMobs.venkrolsiiRange;
            case 3: return SRPConfigMobs.venkrolsiiiRange;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "canInfestBlock",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigMobs;venkrolsiiiRangeY:I"),
            remap = false
    )
    private static int srpmixins_limitInfestationY(int original, @Local(argsOnly = true) int stage) {
        switch (stage) {
            case 1: return SRPConfigMobs.venkrolRangeY;
            case 2: return SRPConfigMobs.venkrolsiiRangeY;
            case 3: return SRPConfigMobs.venkrolsiiiRangeY;
        }
        return original;
    }
}