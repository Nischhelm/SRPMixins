package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.configparse.ParaSpawnEntry;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(targets = "com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityAlafha$EntityAIAirVomitSummon")
public abstract class EntityAlafhaMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries = null;

    @WrapOperation(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnBiomassFromProjectile(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;Lnet/minecraft/entity/EntityLivingBase;)Z", remap = false)
    )
    private boolean srpmixins_sendSummonEntries(EntityParasiteBase entity, String[] mobList, @Nullable EntityLivingBase target, Operation<Boolean> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        return original.call(entity, mobList, target);
    }
}
