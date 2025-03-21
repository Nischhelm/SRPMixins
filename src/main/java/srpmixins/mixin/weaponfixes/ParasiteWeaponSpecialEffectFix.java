package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityParasiteBase.class)
public abstract class ParasiteWeaponSpecialEffectFix extends EntityMob {

    public ParasiteWeaponSpecialEffectFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/EntityMob;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 1),
            cancellable = true
    )
    private void srpmixins_fixParasiteWeaponDmg(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        //Return after super.attackEntityFrom
        cir.setReturnValue(super.attackEntityFrom(source, amount));
    }
}