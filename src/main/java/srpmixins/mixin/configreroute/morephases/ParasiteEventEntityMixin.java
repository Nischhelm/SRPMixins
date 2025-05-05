package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {
    @WrapMethod(method = "getScentBonus", remap = false)
    private static int srpmixins_getScentBonus(byte phase, Operation<Integer> original) {
        if(!SRPConfigSystems.useEvolution || phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return 1;
        return SRPMixinsConfigHandler.morephases.phaseScentBonus[phase];
    }

    @WrapMethod(method = "getScentReactionBonus", remap = false)
    private static byte srpmixins_getScentReactionBonus(byte phase, Operation<Integer> original) {
        if(!SRPConfigSystems.useEvolution) return SRPConfigSystems.scentGoActive;
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return 127;
        return (byte) SRPMixinsConfigHandler.morephases.phaseScentReaction[phase];
    }

    @ModifyExpressionValue(
            method = "getRSchance",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    private static byte srpmixins_getRSchance(byte phase, @Cancellable CallbackInfoReturnable<Double> cir) {
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase)
            cir.setReturnValue(0.0);
        else
            cir.setReturnValue(SRPMixinsConfigHandler.morephases.reinforcementSystemChance[phase]);

        return -69; //don't run original code
    }

    @ModifyExpressionValue(
            method = "spawnBeckonE",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    private static byte srpmixins_spawnBeckonE(byte phase, @Local(argsOnly = true) SRPWorldData data, @Local(argsOnly = true) World world, @Local(argsOnly = true) EntityParasiteBase parasite) {
        if(phase >= 0 && phase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase)
            ParasiteEventEntity.spawnBeckonNE(data, SRPMixinsConfigHandler.morephases.reinforcementSystemChance[phase], world, parasite);
        return -69; //don't run original code
    }
}
