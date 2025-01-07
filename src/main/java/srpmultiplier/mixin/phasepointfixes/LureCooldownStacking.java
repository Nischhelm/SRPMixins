package srpmultiplier.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(BlockEvolutionLure.class)
public abstract class LureCooldownStacking {

    @Redirect(
            method = "func_180639_a",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setCooldown(ILnet/minecraft/world/World;I)V"),
            remap = false
    )
    public void lureCooldownStackingMixin(SRPSaveData instance, int newCooldown, World world, int dim) {
        if (SRPMultiplierConfigHandler.server.lureCooldownStacking) {
            int currentCooldown = instance.getCooldown(world, dim);
            instance.setCooldown(currentCooldown+newCooldown, world, dim);
        } else
            instance.setCooldown(newCooldown, world, dim);
    }
}