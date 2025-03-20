package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOroncoTen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ParaSpawnEntry;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(EntityOroncoTen.class)
public abstract class EntityOroncoTenMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries = null;

    @WrapOperation(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z", remap = false)
    )
    private boolean srpmixins_sendSummonEntries(EntityParasiteBase entity, String[] mobList, int range, @Nullable EntityLivingBase target, Operation<Boolean> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        return original.call(entity, mobList, range, target);
    }
}
