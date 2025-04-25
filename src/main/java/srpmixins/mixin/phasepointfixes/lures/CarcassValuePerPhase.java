package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(BlockEvolutionLure.class)
public abstract class CarcassValuePerPhase {
    @WrapOperation(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z", remap = false)
    )
    private boolean srpmixins_lurePhaseMultiplier(SRPSaveData data, int dimId, int points, boolean isAdding, World world, boolean canChangePhase, Operation<Boolean> original) {
        int multi = SRPMixinsConfigProvider.getLurePhaseMultiplier(data.getEvolutionPhase(dimId));
        return original.call(data, dimId, points * multi, isAdding, world, canChangePhase);
    }
}