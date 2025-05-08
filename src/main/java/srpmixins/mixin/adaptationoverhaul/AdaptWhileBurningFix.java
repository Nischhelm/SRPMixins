package srpmixins.mixin.adaptationoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPMalleable.class)
public abstract class AdaptWhileBurningFix extends EntityLivingBase {
    public AdaptWhileBurningFix(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "attackEntityFrom",
            at = @At(value = "FIELD", target = "Lnet/minecraft/util/DamageSource;IN_FIRE:Lnet/minecraft/util/DamageSource;")
    )
    public DamageSource srpmixins_anyDamageWhileBurningCanFailAdaptation(DamageSource const_IN_FIRE, @Local(argsOnly = true) DamageSource attackingSource){
        //will ignore the damageSource condition and then only check rng
        if(this.isBurning() && !this.isPotionActive(MobEffects.FIRE_RESISTANCE))
            return attackingSource;
        return const_IN_FIRE; //Default behavior
    }
}
