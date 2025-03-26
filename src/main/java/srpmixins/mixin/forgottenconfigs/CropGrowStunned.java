package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SRPEventHandlerBus.class)
public abstract class CropGrowStunned {
    @ModifyExpressionValue(
            method = "cropGrow",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    private byte srpmixins_useAllCropGrowConfigs(byte phase, @Local(argsOnly = true) BlockEvent.CropGrowEvent.Pre event, @Cancellable CallbackInfo ci){
        Random rand = event.getWorld().rand;
        switch (phase) {
            case 9:
                if (rand.nextDouble() < SRPConfigSystems.cropGrowStunnedNine)
                    event.setResult(Event.Result.DENY);
                ci.cancel();
                break;
            case 10:
                if (rand.nextDouble() < SRPConfigSystems.cropGrowStunnedTen)
                    event.setResult(Event.Result.DENY);
                ci.cancel();
                break;
        }

        return phase;
    }
}
