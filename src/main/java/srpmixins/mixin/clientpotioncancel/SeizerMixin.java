package srpmixins.mixin.clientpotioncancel;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityNak;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityNak.class)
public class SeizerMixin {
    @Redirect(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private void cancelClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        if(!instance.world.isRemote)
            instance.addPotionEffect(potionEffect);
    }
}
