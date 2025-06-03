package srpmixins.mixin.potions.clientpotioncancel;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPPotions.class)
public class SRPPotionsMixin {
    @Inject(
            method = "applyStackPotion",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_cancelClientPotion(Potion effect, EntityLivingBase entity, int duration, int amp, CallbackInfo ci){
        if(entity.world.isRemote)
            ci.cancel();
    }

    @WrapWithCondition(
            method = "applySense",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private static boolean srpmixins_cancelClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.world.isRemote;
    }
}
