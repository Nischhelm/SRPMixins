package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(EntityParasiticScent.class)
public abstract class DisableScentDebug extends Entity {

    public DisableScentDebug(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "func_70100_b_",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    private void disableScentDebugMixin(EntityPlayer player, CallbackInfo ci) {
        if (SRPMultiplierConfigHandler.various.disableScentDebug) {
            super.onCollideWithPlayer(player);
            ci.cancel();
        }
    }
}