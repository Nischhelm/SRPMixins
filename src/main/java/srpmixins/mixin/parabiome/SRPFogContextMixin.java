package srpmixins.mixin.parabiome;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.client.FogRendererAccess;
import srpmixins.client.SRPFog;

@Mixin(value = ForgeHooksClient.class, remap = false)
public abstract class SRPFogContextMixin {
    @ModifyReturnValue(method = "getFogDensity", at = @At("RETURN"))
    private static float srpmixins$captureFogContext(float original, EntityRenderer renderer, Entity entity, IBlockState state, float partialTicks, float density) {
        ((FogRendererAccess) renderer).srpmixins$setAmbientFog(original < 0.0F && SRPFog.canApplyFog(entity, state));
        return original;
    }
}
