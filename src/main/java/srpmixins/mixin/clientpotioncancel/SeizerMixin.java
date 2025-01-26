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
            method = "func_70636_d",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;func_70690_d(Lnet/minecraft/potion/PotionEffect;)V"),
            remap = false
    )
    private void cancelClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        if(!instance.world.isRemote)
            instance.addPotionEffect(potionEffect);
    }
}
