package srpmixins.mixin.clientpotioncancel;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityNak;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityNak.class)
public class SeizerMixin {
    @WrapWithCondition(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private boolean cancelClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.world.isRemote;
    }
}
