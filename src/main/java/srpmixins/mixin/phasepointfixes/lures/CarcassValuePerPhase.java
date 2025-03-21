package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(BlockEvolutionLure.class)
public abstract class CarcassValuePerPhase {
    @ModifyArgs(
            method="onBlockActivated",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z", remap = false)
    )
    private void srpmixins_lurePhaseMultiplier(Args args, @Local SRPSaveData data) {
        int dimension = args.get(0);
        int multi = SRPMixinsConfigProvider.getLurePhaseMultiplier(data.getEvolutionPhase(dimension));
        int points = args.get(1);
        args.set(1, points * multi);
    }
}