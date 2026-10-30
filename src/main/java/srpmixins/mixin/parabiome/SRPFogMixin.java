package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import srpmixins.client.SRPFog;
import srpmixins.config.SRPMixinsConfigHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPFogMixin {
    //SRP hard-switches to the parasite fog color above 20% density. Instead, gradually blend from the current fog color (vanilla or other mods) to the parasite color as density rises
    @WrapMethod(method = "onEvent(Lnet/minecraftforge/client/event/EntityViewRenderEvent$FogColors;)V", remap = false)
    private void srpmixins$blendFogColor(EntityViewRenderEvent.FogColors event, Operation<Void> original) {
        if (!SRPFog.terrainFogEnabled()) return;
        if (!SRPMixinsConfigHandler.parabiome.smoothBiomeFog) {
            original.call(event);
            return;
        }
        if (SRPFog.canApplyFog(event.getEntity(), event.getState())) {
            float weight = SRPFog.Transition.colorWeight(SRPFog.State.density(), SRPConfigWorld.biomeFogDensity);
            event.setRed(event.getRed() + (SRPConfigWorld.biomeFogRed / 255.0F - event.getRed()) * weight);
            event.setGreen(event.getGreen() + (SRPConfigWorld.biomeFogGreen / 255.0F - event.getGreen()) * weight);
            event.setBlue(event.getBlue() + (SRPConfigWorld.biomeFogBlue / 255.0F - event.getBlue()) * weight);
        }
        //don't run original
    }

    @WrapMethod(method = "onEvent(Lnet/minecraftforge/client/event/EntityViewRenderEvent$FogDensity;)V", remap = false)
    private void srpmixins$useAmbientDensity(EntityViewRenderEvent.FogDensity event, Operation<Void> original) {
        if (!SRPFog.terrainFogEnabled()) return;
        if (!SRPMixinsConfigHandler.parabiome.smoothBiomeFog) original.call(event);
    }

}
