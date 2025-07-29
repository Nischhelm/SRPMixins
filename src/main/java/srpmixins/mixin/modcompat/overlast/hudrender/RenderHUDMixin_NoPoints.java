package srpmixins.mixin.modcompat.overlast.hudrender;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.overlast.gui.RenderHUD;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderHUD.class)
public abstract class RenderHUDMixin_NoPoints {
    @WrapWithCondition(
            method = "renderOverlay",
            at = @At(value = "INVOKE", target = "Lcom/overlast/gui/RenderHUD;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V")
    )
    private boolean srpmixins_removePointString(RenderHUD instance, FontRenderer fontRendererIn, String text, int x, int y, int color){
        return false;
    }
}
