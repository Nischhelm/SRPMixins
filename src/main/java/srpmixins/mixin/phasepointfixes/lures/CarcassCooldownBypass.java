package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(SRPSaveData.class)
public abstract class CarcassCooldownBypass {
    @Redirect(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCooldown(Lnet/minecraft/world/World;I)I"),
            remap = false
    )
    private int cancelIfMinus(SRPSaveData instance, World world, int dim, @Local(argsOnly = true, ordinal = 1) int points, @Local(argsOnly = true, ordinal = 0) boolean isAdding) {
        int cd = instance.getCooldown(world, dim);
        if (SRPMixinsConfigHandler.lures.fixCarcassDuringCooldown && cd != 0 && points < 0 && isAdding)
            return 0;
        return cd;
    }
}