package srpmixins.mixin.adaptationoverhaul;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(SRPEventHandlerBus.class)
public class ArmorAdaptationCancel {
    @ModifyExpressionValue(
            method = "entityHurt",
            at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"),
            remap = false
    )
    private boolean srpmixins_cancelOldHandling(boolean original){
        return false;
    }
}
