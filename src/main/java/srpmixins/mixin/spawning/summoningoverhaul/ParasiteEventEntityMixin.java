package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanSummon;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityBiomass;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileBiomass;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ISummonsByUUID;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {
    @WrapOperation(
            method = {
                    "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
                    "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z"},
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanSummon;addID(II)V"),
            remap = false
    )
    private static void srpmixins_addByUUID(EntityCanSummon instance, int id, int points, Operation<Void> original, @Local EntityLiving entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }

    @WrapOperation(
            method = {"spawnBiomassFromVomit", "spawnBiomassFromBeckon"},
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanSummon;addID(II)V"),
            remap = false
    )
    private static void srpmixins_addByUUID(EntityCanSummon instance, int id, int points, Operation<Void> original, @Local EntityBiomass entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }

    @WrapOperation(
            method = "spawnBiomassFromProjectile",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanSummon;addID(II)V"),
            remap = false
    )
    private static void srpmixins_addByUUID(EntityCanSummon instance, int id, int points, Operation<Void> original, @Local EntityProjectileBiomass entitySpawned){
        ((ISummonsByUUID) instance).srpmixins$addSummon(entitySpawned.getUniqueID(), points);
    }
}
