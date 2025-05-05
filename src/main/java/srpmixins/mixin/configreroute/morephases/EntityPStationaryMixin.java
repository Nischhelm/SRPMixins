package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityPStationary.class)
public abstract class EntityPStationaryMixin {
    @WrapMethod(method = "getRSChance", remap = false)
    public double srpmixins_getRSChance(byte phase, Operation<Double> original) {
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return 0.0;
        return SRPMixinsConfigHandler.morephases.reinforcementSystemChance[phase];
    }
}
