package srpmixins.mixin.parabiome.compat;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.client.fog.SrpFogRange;
import srpmixins.client.fog.SrpFogState;

@Pseudo
@Mixin(targets = "org.orecruncher.dsurround.client.handlers.FogHandler", remap = false)
public abstract class DynamicSurroundingsFogMixin {
    @Inject(method = "fogRenderEvent", at = @At("RETURN"))
    private void srpmixins$applySrpFogRange(EntityViewRenderEvent.RenderFogEvent event, CallbackInfo ci) {
        double frame = event.getEntity().ticksExisted + event.getRenderPartialTicks();
        if (!SrpFogState.consumeFogFrame(frame)) return;
        float density = SrpFogState.density();
        if (density <= 0.0F) return;
        SrpFogRange.apply(density);
    }
}
