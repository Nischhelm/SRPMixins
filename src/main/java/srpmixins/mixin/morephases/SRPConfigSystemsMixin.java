package srpmixins.mixin.morephases;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPConfigSystems.class)
public abstract class SRPConfigSystemsMixin {
    @ModifyConstant(
            method = "initevolutionConfig",
            constant = @Constant(intValue = 11),
            remap = false
    )
    private static int srpmixins_increaseMaxPhase(int constant){
        return EarlyConfigReader.getInt("Max Phase", SRPMixinsConfigHandler.morephases.maxEvolutionPhase) + 1;
    }

    @ModifyConstant(
            method = "initevolutionConfig",
            constant = @Constant(intValue = 9),
            remap = false
    )
    private static int srpmixins_increaseMaxPhase_fromOld8(int constant){
        return EarlyConfigReader.getInt("Max Phase", SRPMixinsConfigHandler.morephases.maxEvolutionPhase) + 1;
    }
}
