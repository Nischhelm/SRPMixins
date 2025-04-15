package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityDropPod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.configparse.ParaSpawnEntry;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(EntityDropPod.class)
public abstract class EntityDropPodMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries_summon = null;

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z"),
            remap = false
    )
    private boolean srpmixins_sendSummonEntries(EntityParasiteBase entity, String[] mobList, int range, double tx, double ty, double tz, @Nullable EntityLivingBase target, boolean pod, Operation<Boolean> original){
        if(srpmixins$spawnEntries_summon == null) srpmixins$spawnEntries_summon = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries_summon);
        return original.call(entity, mobList, range, tx, ty, tz, target, pod);
    }
}
