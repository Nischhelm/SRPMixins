package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.handlers.SRPMixinsConfigHandler;

import java.io.PrintStream;

@Mixin(EntityParasiticScent.class)
public abstract class DisableScentDebug extends Entity {

    public DisableScentDebug(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "onCollideWithPlayer",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void disableScentDebugMixin(EntityPlayer player, CallbackInfo ci) {
        if (SRPMixinsConfigHandler.various.disableScentDebug) {
            super.onCollideWithPlayer(player);
            ci.cancel();
        }
    }

    @Redirect(
            method = "placeWaves",
            at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"),
            remap = false
    )
    void disableScentDebugMixin(PrintStream instance, String x){
        if (!SRPMixinsConfigHandler.various.disableScentDebug)
            instance.println(x);
        //no op if disabled
    }
}