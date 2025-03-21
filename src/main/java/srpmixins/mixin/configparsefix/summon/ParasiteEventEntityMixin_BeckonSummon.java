package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ParaSpawnEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin_BeckonSummon {
    @Unique private static final Map<String, List<ParaSpawnEntry>> srpmixins$spawnEntries_summon_beckonNE = new HashMap<>();

    @WrapOperation(
            method = "spawnBeckonNE",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z"),
            remap = false
    )
    private static boolean srpmixins_sendSummonEntries(EntityParasiteBase entity, String[] mobList, int range, @Nullable EntityLivingBase target, Operation<Boolean> original){
        if(mobList.length == 1) {
            if (!srpmixins$spawnEntries_summon_beckonNE.containsKey(mobList[0]))
                srpmixins$spawnEntries_summon_beckonNE.put(mobList[0], ParaSpawnEntry.parseMobList(mobList, false));
            ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries_summon_beckonNE.get(mobList[0]));
        }
        return original.call(entity, mobList, range, target);
    }
}
