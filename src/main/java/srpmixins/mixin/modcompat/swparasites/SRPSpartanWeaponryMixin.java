package srpmixins.mixin.modcompat.swparasites;

import com.existingeevee.swparasites.SRPSpartanWeaponry;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPSpartanWeaponry.class)
public class SRPSpartanWeaponryMixin {
    @Inject(
            method = "onLoadComplete",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void srpmixins_cancelOriginalHandling(FMLLoadCompleteEvent event, CallbackInfo ci){
        ci.cancel();
    }
}
