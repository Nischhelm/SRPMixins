package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanSummon;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityBiomass;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ISummonsByUUID;

@Mixin(EntityBiomass.class)
public abstract class EntityBiomassMixin {
    @WrapOperation(
            method = "explode",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanSummon;addID(II)V"),
            remap = false
    )
    private void srpmixins_addByUUID(EntityCanSummon instance, int id, int points, Operation<Void> original, @Local EntityLiving entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }
}
