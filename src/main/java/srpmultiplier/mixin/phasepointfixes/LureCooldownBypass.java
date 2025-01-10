package srpmultiplier.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPSaveData.class)
public abstract class LureCooldownBypass {
    @Unique private static int points;
    @Unique private static boolean isAdding;

    @Inject(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCooldown(Lnet/minecraft/world/World;I)I"),
            remap = false
    )
    private void saveParams(int id, int in, boolean plus, World worldIn, boolean canChangePhase, CallbackInfoReturnable<Boolean> cir){
        points = in;
        isAdding = plus;
    }

    @Redirect(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCooldown(Lnet/minecraft/world/World;I)I"),
            remap = false
    )
    private int cancelIfMinus(SRPSaveData instance, World world, int dim) {
        int cd = instance.getCooldown(world, dim);
        if (SRPMultiplierConfigHandler.lures.fixCarcassDuringCooldown && cd != 0 && points < 0 && isAdding)
            return 0;
        return cd;
    }
}