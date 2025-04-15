package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityDorpa;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityDorpa.class)
public abstract class AssimBigSpiderSpawnFix {
    @ModifyReturnValue(
            method = "canSpawnByIDData",
            at = @At(value = "RETURN"),
            remap = false
    )
    private int srpmixins_fixSimSpiderMinAssimilations(int original) {
        return SRPMixinsConfigHandler.coth.assimBigSpiderMinAssimilations;
    }
}