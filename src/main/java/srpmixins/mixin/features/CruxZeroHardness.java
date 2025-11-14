package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityCrux;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCrux.class)
public abstract class CruxZeroHardness {
    @ModifyExpressionValue(
            method = "checkBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getBlockHardness(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F")
    )
    private float srpmixins_alsoAllowZeroHardness(float value) {
        if(value == 0) return Float.MIN_VALUE; //will fail bHard <= 0
        return value;
    }
}
