package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityWaveShock;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityPStationary.class,
        EntityOrch.class,
        EntityPheon.class,
        EntityFlam.class,
        EntityWaveShock.class,
        EntityParasiteBase.class
})
public abstract class BlockBreakZeroHardness {
    @Definition(id = "bHard", local = @Local(type = float.class))
    @Expression(value = "bHard > 0.0")
    @WrapOperation(
            method = "skillBreakBlocks",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean srpmixins_alsoAllowZeroHardness(float left, float right, Operation<Boolean> original) {
        return original.call(left, right) || left == right; //same as left >= 0.0F
    }
}
