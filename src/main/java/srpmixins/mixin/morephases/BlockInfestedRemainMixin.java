package srpmixins.mixin.morephases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRemain;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(BlockInfestedRemain.class)
public abstract class BlockInfestedRemainMixin {
    @WrapMethod(method = "getEvoResidue", remap = false)
    private int srpmixins_getEvoResidue(byte phase, Operation<Integer> original) {
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return -5;
        return SRPMixinsConfigHandler.morephases.phaseResidue[phase];
    }
}
