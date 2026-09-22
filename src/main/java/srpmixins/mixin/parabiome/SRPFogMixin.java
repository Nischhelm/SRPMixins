package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import srpmixins.client.fog.SrpFogState;
import srpmixins.client.fog.SrpFogRange;
import srpmixins.client.fog.SrpFogTransition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SRPEventHandlerBus.class, remap = false, priority = 900)
public abstract class SRPFogMixin {
    @Unique
    private static final boolean srpmixins$dynamicSurroundingsLoaded = Loader.isModLoaded("dsurround");

    @Unique
    private static boolean srpmixins$canApplyFog(EntityViewRenderEvent event) {
        return !event.getState().getMaterial().isLiquid()
                && (!(event.getEntity() instanceof EntityLivingBase)
                || !((EntityLivingBase) event.getEntity()).isPotionActive(MobEffects.BLINDNESS));
    }

    @Inject(method = "onEvent(Lnet/minecraftforge/client/event/EntityViewRenderEvent$FogColors;)V",
            at = @At("HEAD"), cancellable = true)
    private void srpmixins$blendFogColor(EntityViewRenderEvent.FogColors event, CallbackInfo ci) {
        if (srpmixins$canApplyFog(event)) {
            float weight = SrpFogTransition.colorWeight(SrpFogState.density(), SRPConfigWorld.biomeFogDensity);
            event.setRed(event.getRed() + (SRPConfigWorld.biomeFogRed / 255.0F - event.getRed()) * weight);
            event.setGreen(event.getGreen() + (SRPConfigWorld.biomeFogGreen / 255.0F - event.getGreen()) * weight);
            event.setBlue(event.getBlue() + (SRPConfigWorld.biomeFogBlue / 255.0F - event.getBlue()) * weight);
        }
        ci.cancel();
    }

    @Inject(method = "onEvent(Lnet/minecraftforge/client/event/EntityViewRenderEvent$FogDensity;)V",
            at = @At("HEAD"), cancellable = true)
    private void srpmixins$keepNormalFogSetup(EntityViewRenderEvent.FogDensity event, CallbackInfo ci) {
        if (!event.isCanceled() && srpmixins$canApplyFog(event)) {
            SrpFogState.prepareFogFrame(event.getEntity().ticksExisted + event.getRenderPartialTicks());
        } else {
            SrpFogState.clearFogFrame();
        }
        ci.cancel();
    }

    @SubscribeEvent
    public void srpmixins$beginFogFrame(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) SrpFogState.clearFogFrame();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void srpmixins$applyFogRange(EntityViewRenderEvent.RenderFogEvent event) {
        if (srpmixins$dynamicSurroundingsLoaded) return;
        double frame = event.getEntity().ticksExisted + event.getRenderPartialTicks();
        if (!SrpFogState.consumeFogFrame(frame) || !srpmixins$canApplyFog(event)) return;
        float density = SrpFogState.density();
        if (density <= 0.0F) return;
        SrpFogRange.apply(density);
    }
}
