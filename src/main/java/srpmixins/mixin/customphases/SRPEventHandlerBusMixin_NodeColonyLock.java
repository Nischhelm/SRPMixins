package srpmixins.mixin.customphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin_NodeColonyLock {
    @WrapOperation(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;applyBonuses(IIB)V"),
            remap = false
    )
    public void srpmixins_lockNodeColonyBonuses(EntityParasiteBase entity, int colony, int node, byte phase, Operation<Void> original) {
        if(phase < SRPConfigSystems.evolutionNodeUnlock) node = 0;
        if(phase < SRPConfigSystems.evolutionColonyUnlock) colony = 0;
        original.call(entity, colony, node, phase);
    }
}