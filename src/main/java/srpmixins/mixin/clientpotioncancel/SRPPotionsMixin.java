package srpmixins.mixin.clientpotioncancel;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintStream;

@Mixin(SRPPotions.class)
public class SRPPotionsMixin {
    @Inject(
            method = "applyStackPotion",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void cancelClientPotion(Potion effect, EntityLivingBase in, int duration, int amp, CallbackInfo ci){
        if(in.world.isRemote)
            ci.cancel();
    }

    @Redirect(
            method = "applySense",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;func_70690_d(Lnet/minecraft/potion/PotionEffect;)V"),
            remap = false
    )
    private static void cancelClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        if(!instance.world.isRemote)
            instance.addPotionEffect(potionEffect);
    }

    @Redirect(
            method = "applyStackPotion",
            at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"),
            remap = false
    )
    private static void cancelDebugMsg(PrintStream instance, String x){
        //no op, dont send debug msg
    }
}
