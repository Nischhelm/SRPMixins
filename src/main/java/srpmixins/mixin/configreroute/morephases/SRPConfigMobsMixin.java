package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPConfigMobs.class)
public abstract class SRPConfigMobsMixin {
    @ModifyConstant(
            method = "initmudoConfig",
            constant = @Constant(intValue = 9),
            remap = false
    )
    private static int srpmixins_increaseMaxPhase(int constant){
        return EarlyConfigReader.getInt("Max Phase", SRPMixinsConfigHandler.morephases.maxEvolutionPhase) + 1;
    }
}
