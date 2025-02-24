package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPSaveData.class)
public abstract class CarcassCooldownBypass {
    @ModifyExpressionValue(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCooldown(Lnet/minecraft/world/World;I)I"),
            remap = false
    )
    private int cancelIfMinus(int original, @Local(argsOnly = true, ordinal = 1) int points, @Local(argsOnly = true, ordinal = 0) boolean isAdding) {
        //Chunk Phases handles it internally in CapabilityEvoPoints
        if(SRPMixinsConfigHandler.chunkphases.enabled) return original;

        //No cooldown point change block if reducing points
        if (SRPMixinsConfigHandler.lures.fixCarcassDuringCooldown && original != 0 && points < 0 && isAdding) return 0;

        //Default behavior
        return original;
    }
}