package srpmixins.mixin.modcompat.overlast.hudrender;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.overlast.gui.RenderHUD;
import com.overlast.gui.StatBar;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.compat.overlast.IStatBar;
import srpmixins.config.SRPConfigProvider;

@Mixin(RenderHUD.class)
public abstract class RenderHUDMixin {
    @Shadow(remap = false) @Final private static StatBar EVOLUTION_BAR;

    @WrapOperation(
            method = "retrieveStats",
            at = @At(value = "INVOKE", target = "Lcom/overlast/gui/StatBar;setValue(F)V"),
            remap = false
    )
    private static void srpmixins_skipOriginalCheck(StatBar instance, float value, Operation<Void> original, @Share("actualPhase")LocalIntRef actualPhase, @Local(name = "phase") LocalIntRef originalPhase){
        if(instance == EVOLUTION_BAR) {
            actualPhase.set(originalPhase.get());
            originalPhase.set(-3);
        }
        original.call(instance, value);
    }

    @Inject(
            method = "retrieveStats",
            at = @At(value = "TAIL"),
            remap = false
    )
    private static void srpmixins_useCustomPhasesIfNeeded(CallbackInfo ci, @Share("actualPhase")LocalIntRef actualPhase){
        int phase = actualPhase.get();
        if(phase < 0) {
            if(phase == -2) ((IStatBar) EVOLUTION_BAR).srpmixins$setMinValue(-200);
            else if(phase == -1) ((IStatBar) EVOLUTION_BAR).srpmixins$setMinValue(-100);
            EVOLUTION_BAR.setMaxValue(0);
        }
        else if(phase < SRPConfigProvider.getMaxPhase()) {
            ((IStatBar) EVOLUTION_BAR).srpmixins$setMinValue(SRPConfigProvider.getPhaseMinPoints((byte) phase));
            EVOLUTION_BAR.setMaxValue(SRPConfigProvider.getPhaseMinPoints((byte) (phase + 1)));
        } else {
            ((IStatBar) EVOLUTION_BAR).srpmixins$setMinValue(SRPConfigProvider.getPhaseMinPoints(SRPConfigProvider.getMaxPhase()));
            EVOLUTION_BAR.setMaxValue(SRPConfigSystems.phaseTenTotalPoints);
        }
    }

    @Redirect(
            method = "renderOverlay",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;I)I"),
            remap = false
    )
    private int srpmixins_dontParse(String digit, int firstChar){
        return 0xFFFFFF;
    }

    @WrapOperation(
            method = "renderOverlay",
            at = @At(value = "INVOKE", target = "Lcom/overlast/gui/RenderHUD;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V")
    )
    private void srpmixins_giveThatStringSomeSpace(RenderHUD instance, FontRenderer fontRenderer, String string, int x, int y, int color, Operation<Void> original){
        instance.drawString(fontRenderer, string, x + 10 - fontRenderer.getStringWidth(string), y, 0xFFFFFF);
    }
}
