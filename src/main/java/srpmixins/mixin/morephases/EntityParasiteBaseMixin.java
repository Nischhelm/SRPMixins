package srpmixins.mixin.morephases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin {
    @Shadow(remap = false) protected double killcount;

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;phaseCreated:B", remap = false)
    )
    private byte srpmixins_addKillCount(byte phase) {
        if(phase >= 0 && phase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase)
            this.killcount += SRPMixinsConfigHandler.morephases.phaseKillCountPlus[phase];
        return -69; //don't run original code
    }
}
