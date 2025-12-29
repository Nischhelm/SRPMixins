package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfPlayer;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityInfPlayer.class)
public abstract class AssimPlayerFix_Threshold {
    @ModifyReturnValue(
            method = "canSpawnByIDData",
            at = @At(value = "RETURN"),
            remap = false
    )
    private int srpmixins_fixSimSpiderMinAssimilations(int original) {
        return SRPMixinsConfigHandler.coth.assimPlayerMinAssimilations;
    }
}