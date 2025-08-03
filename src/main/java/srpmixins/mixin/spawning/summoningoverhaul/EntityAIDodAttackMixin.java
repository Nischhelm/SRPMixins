package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIDodAttack;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPDispatcher;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ISummonsByUUID;

@Mixin(EntityAIDodAttack.class)
public abstract class EntityAIDodAttackMixin {
    @WrapOperation(
            method = "spawnDet",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPDispatcher;addID(II)V"),
            remap = false
    )
    private void srpmixins_addByUUID(EntityPDispatcher instance, int id, int points, Operation<Void> original, @Local(argsOnly = true) EntityPStationary entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }
}
