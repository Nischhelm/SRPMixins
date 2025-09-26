package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPInfected.class)
public abstract class InfectNearbyFix_Assim {
    @WrapWithCondition(
            method = "InfectNearby",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", remap = true),
            remap = false
    )
    private boolean srpmixins_checkForVisibility(EntityLivingBase instance, PotionEffect potionEffect, @Local(name = "entity") EntityLivingBase entity, @Local(name = "mob") EntityLivingBase mob){
        return entity.canEntityBeSeen(mob);
    }
}
