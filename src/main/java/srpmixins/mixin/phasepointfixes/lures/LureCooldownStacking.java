package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockEvolutionLure.class)
public abstract class LureCooldownStacking {

    @ModifyArg(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setCooldown(ILnet/minecraft/world/World;I)V", remap = false),
            index = 0
    )
    private int lureCooldownStacking(int original, @Local(argsOnly = true) World world, @Local SRPSaveData data) {
        return original + data.getCooldown(world, world.provider.getDimension());
    }
}