package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityWaveShock;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = {
        EntityPStationary.class,
        EntityOrch.class,
        EntityPheon.class,
        EntityFlam.class,
        EntityWaveShock.class,
        EntityParasiteBase.class
})
public abstract class BlockBreakZeroHardness {
    @ModifyVariable(
            method = "skillBreakBlocks",
            at = @At(value = "LOAD", ordinal = 1),
            name = "bHard",
            remap = false
    )
    private float srpmixins_alsoAllowZeroHardness(float value) {
        if(value == 0) return Float.MIN_VALUE; //will not fail bHard > 0
        return value;
    }

    //This would be better but won't work with MixinBooter2FermiumBooter in its current state
    /*@Definition(id = "bHard", local = @Local(type = float.class))
    @Expression(value = "bHard > 0.0")
    @WrapOperation(
            method = "skillBreakBlocks",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean srpmixins_alsoAllowZeroHardness(float left, float right, Operation<Boolean> original) {
        return original.call(left, right) || left == right; //same as left >= 0.0F
    }*/
}
