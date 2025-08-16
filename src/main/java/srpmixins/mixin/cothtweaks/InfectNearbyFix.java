package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(SRPEffectBase.class)
public abstract class InfectNearbyFix {
    @ModifyExpressionValue(
            method = "InfectNearby",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z", remap = true),
            slice = @Slice(from = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPPotions;EPEL_E:Lnet/minecraft/potion/Potion;"), to = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;epelChanceCOTH:D")),
            remap = false
    )
    private boolean srpmixins_alsoCheckForVisibilityIfCamouflaged(boolean original, @Local(name = "entity") EntityLivingBase entity, @Local(name = "mob") EntityLivingBase mob){
        return original && entity.canEntityBeSeen(mob);
    }
}
