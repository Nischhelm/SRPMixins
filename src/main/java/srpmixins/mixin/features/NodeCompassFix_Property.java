package srpmixins.mixin.features;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(targets = "com.dhanantry.scapeandrunparasites.item.ItemNodeColonyCompass$1")
public abstract class NodeCompassFix_Property {
    @ModifyExpressionValue(
            method = "apply",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;isSurfaceWorld()Z")
    )
    private boolean srpmixins_skipUselessCheck(boolean original){
        return true; //no need to copy this behavior from normal compass
    }

    @WrapOperation(
            method = "apply",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;positiveModulo(FF)F")
    )
    private float srpmixins_fixAngle(float numerator, float denominator, Operation<Float> original){
        //turn 17 degrees (1.5 registered icons) so the needle actually points at the node
        return original.call(numerator, denominator)+0.047F;
    }
}
