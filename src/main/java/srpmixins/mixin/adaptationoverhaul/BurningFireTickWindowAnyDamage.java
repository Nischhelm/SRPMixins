package srpmixins.mixin.adaptationoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPMalleable.class)
public abstract class BurningFireTickWindowAnyDamage extends EntityParasiteBase {

    public BurningFireTickWindowAnyDamage(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "attackEntityFrom",
            at = @At(value = "FIELD", target = "Lnet/minecraft/util/DamageSource;IN_FIRE:Lnet/minecraft/util/DamageSource;"),
            remap = true
    )
    public DamageSource srpmixins_anyDamageWhenBurningSetFailWindow(DamageSource original, @Local(argsOnly = true) DamageSource source){
        if(this.isBurning()){
//            SRPMixins.LOGGER.log(Level.INFO, "Burning! Adaption Fire Tick Window set by {}", source.damageType);
            return source;
        }
        return original;
    }

    @Unique
    @Override
    public boolean isBurning(){
        return super.isBurning() && !this.isPotionActive(MobEffects.FIRE_RESISTANCE);
    }
}
