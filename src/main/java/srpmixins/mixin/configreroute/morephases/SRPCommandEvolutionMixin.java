package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPCommandEvolution.class)
public abstract class SRPCommandEvolutionMixin {
    @ModifyConstant(
            method = "execute",
            constant = @Constant(intValue = 11)
    )
    private int srpmixins_modifyMaxPhase(int constant){
        return SRPMixinsConfigHandler.morephases.maxEvolutionPhase + 1;
    }

    @WrapMethod(method = "getNeededPoints", remap = false)
    private static int srpmixins_getNeededPoints(byte phase, Operation<Integer> original){
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return 0;
        return SRPMixinsConfigHandler.morephases.phaseKills[phase];
    }
}
