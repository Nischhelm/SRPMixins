package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPreeminent;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ISummonsByUUID;

@Mixin(EntityPPreeminent.class)
public abstract class EntityPPreeminentMixin {
    @WrapOperation(
            method = "summonFlam",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPPreeminent;addID(II)V"),
            remap = false
    )
    private void srpmixins_addByUUID(EntityPPreeminent instance, int id, int points, Operation<Void> original, @Local EntityFlam entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }
}
