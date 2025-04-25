package srpmixins.mixin.morephases;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin {
    @WrapMethod(method = "getSleepPointP", remap = false)
    private int srpmixins_getSleepPointP(byte phase, Operation<Integer> original){
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return 0;
        return SRPMixinsConfigHandler.morephases.sleepPenalty[phase];
    }

    @WrapMethod(method = "setCOTH", remap = false)
    private void srpmixins_setCOTH(EntityLivingBase target, byte phase, Operation<Void> original) {
        if(phase >= 0 && phase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase)
            if (!target.isChild() && target.getRNG().nextFloat() < SRPMixinsConfigHandler.morephases.mobSpawningCOTHChance[phase])
                target.addPotionEffect(new PotionEffect(SRPPotions.COTH_E, 3600, 0, false, false));
    }

    @ModifyExpressionValue(
            method = "cropGrow",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    private byte srpmixins_cropGrow(byte phase, @Local(argsOnly = true) BlockEvent.CropGrowEvent.Pre event, @Cancellable CallbackInfo ci){
        if(phase >= 0 && phase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase)
            if (event.getWorld().rand.nextFloat() < SRPMixinsConfigHandler.morephases.cropGrowStunned[phase])
                event.setResult(Event.Result.DENY);
        ci.cancel();
        return phase;
    }
}
