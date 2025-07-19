package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPEventHandlerBus.class)
public class NodeColonyResetFix {
    @ModifyExpressionValue(
            method = "serverTick",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;dayTickValue:I"),
            remap = false
    )
    private int srpmixins_checkMoreOften(int original){
        return 100;
    }
}
