package srpmixins.mixin.adaptationoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//By kotlinprogrammer / RLMixins, modified+fixed
@Mixin(EntityPMalleable.class)
public abstract class AdaptToIndirectFix {

    @WrapOperation(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;getImmediateSource()Lnet/minecraft/entity/Entity;", ordinal = 0)
    )
    private Entity srpmixins_skipImmediateSourceCheckIfIndirect(DamageSource source, Operation<Entity> original) {
        if (source instanceof EntityDamageSourceIndirect) return null; //adapt to dmg type directly
        return original.call(source);
    }

    @WrapOperation(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;getTrueSource()Lnet/minecraft/entity/Entity;", ordinal = 3)
    )
    private Entity srpmixins_skipTrueSourceCheckIfIndirect(DamageSource source, Operation<Entity> original) {
        if (source instanceof EntityDamageSourceIndirect) return null; //adapt to dmg type directly
        return original.call(source);
    }
}