package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {
    @WrapMethod(method = "canSpawninPhase", remap = false)
    private static boolean srpmixins_canSpawninPhase(byte phase, EntityParasiteBase parasite, Operation<Boolean> original) {
        byte type = parasite.getParasiteType();
        if (phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return false;

        if (SRPMixinsConfigHandler.morephases.phaseMinParasiteID[phase] < type && type < SRPMixinsConfigHandler.morephases.phaseMaxParasiteID[phase]) {
            parasite.setCreatedPhase(phase);
            return true;
        }
        return false;
    }
}
