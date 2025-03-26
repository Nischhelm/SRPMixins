package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParasiteEventEntity.class)
public abstract class ScentBonus {
    @ModifyReturnValue(
            method = "getScentBonus",
            at = @At("RETURN"),
            remap = false
    )
    private static int srpmixins_useAllScentBonusConfigs(int original, @Local(argsOnly = true) byte phase){
        if(SRPConfigSystems.useEvolution)
            switch (phase){
                case 9: return SRPConfigSystems.phaseScentBonusNine;
                case 10: return SRPConfigSystems.phaseScentBonusTen;
            }
        return original;
    }
}
