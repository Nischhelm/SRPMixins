package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(EntityAINexusGrow.class)
public abstract class EntityAINexusGrowMixin {
    @Shadow(remap = false) private boolean canGrow;
    @Shadow(remap = false) private byte venkrolCurrentStage;
    @Shadow(remap = false) @Final private EntityPStationaryArchitect parent;

    @ModifyExpressionValue(
            method = "checkPhase",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    public byte srpmixins_checkPhase(byte phase, @Cancellable CallbackInfo ci) {
        if(this.venkrolCurrentStage <= 0 || this.venkrolCurrentStage > 3) return phase;

        this.canGrow =
                phase >= 0 && phase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase &&
                this.parent.getRNG().nextFloat() >= SRPMixinsConfigProvider.nexusGrowStunChance.get(phase).get(this.venkrolCurrentStage-1);

        ci.cancel();
        return phase;
    }
}
