package srpmixins.mixin.parabiome;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.client.FogRendererAccess;
import srpmixins.client.SRPFog;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityRenderer.class)
public abstract class SRPFogRendererMixin implements FogRendererAccess {
    @Unique
    private boolean srpmixins$ambientFog;

    @Override
    public void srpmixins$setAmbientFog(boolean ambientFog) {
        srpmixins$ambientFog = ambientFog;
    }

    @Inject(method = "setupFog", at = @At("HEAD"))
    private void srpmixins$resetFogContext(int startCoords, float partialTicks, CallbackInfo ci) {
        srpmixins$ambientFog = false;
    }

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void srpmixins$composeAmbientFog(int startCoords, float partialTicks, CallbackInfo ci) {
        if (!srpmixins$ambientFog || !SRPMixinsConfigHandler.parabiome.smoothBiomeFog || !SRPFog.terrainFogEnabled()) return;
        float density = SRPFog.State.density();
        if (density > 0.0F) SRPFog.applyAmbientFog(density);
    }
}
