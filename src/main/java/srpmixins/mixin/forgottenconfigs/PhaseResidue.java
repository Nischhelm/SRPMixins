package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRemain;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockInfestedRemain.class)
public abstract class PhaseResidue {
    @ModifyReturnValue(
            method = "getEvoResidue",
            at = @At("RETURN"),
            remap = false
    )
    private int srpmixins_useAllPhaseResidueConfigs(int original, @Local(argsOnly = true) byte phase) {
        switch (phase) {
            case 9:  return SRPConfigSystems.phaseResidueNine;
            case 10: return SRPConfigSystems.phaseResidueTen;
        }
        return original;
    }
}
