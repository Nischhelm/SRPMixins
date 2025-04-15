package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIVenkrolSummon;
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

@Mixin(EntityAIVenkrolSummon.class)
public abstract class EntityAIVenkrolSummonMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries = null;

    @WrapOperation(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z", remap = false)
    )
    private boolean srpmixins_sendSummonEntries(EntityParasiteBase entity, String[] mobList, int range, double tx, double ty, double tz, @Nullable EntityLivingBase target, boolean pod, Operation<Boolean> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        return original.call(entity, mobList, range, tx, ty, tz, target, pod);
    }
}
