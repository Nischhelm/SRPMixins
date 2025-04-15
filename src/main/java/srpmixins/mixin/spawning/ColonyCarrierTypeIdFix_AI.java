package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIGetFollowers;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityVesta;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAIGetFollowers.class)
public abstract class ColonyCarrierTypeIdFix_AI {
    @WrapOperation(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;getParasiteType()B", ordinal = 2),
            remap = false
    )
    private byte srpmixins_fixColCarrierTypeId(EntityParasiteBase instance, Operation<Byte> original){
        if(instance instanceof EntityVesta) return 31;  //this is where colony carrier needs to have a lower type id than it should have
        return original.call(instance);
    }
}
