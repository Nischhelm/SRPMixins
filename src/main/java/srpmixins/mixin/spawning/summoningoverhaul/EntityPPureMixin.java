package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPure;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ISummonsByUUID;

@Mixin(EntityPPure.class)
public abstract class EntityPPureMixin{

    @WrapOperation(
            method = "spawnT",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPPure;addID(II)V"),
            remap = false
    )
    private void srpmixins_addByUUID(EntityPPure instance, int id, int points, Operation<Void> original, @Local EntityPStationary entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }
}
