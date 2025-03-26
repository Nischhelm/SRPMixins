package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityParasiteBase.class)
public abstract class ParasiteWeaponSpecialEffectFix extends EntityMob {

    public ParasiteWeaponSpecialEffectFix(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/EntityMob;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
    )
    private boolean srpmixins_fixParasiteWeaponDmg(boolean original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        //Return after all super.attackEntityFrom's
        cir.setReturnValue(original);
        return original;
    }
}