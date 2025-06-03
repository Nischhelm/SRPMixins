package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPRooter;
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

@Mixin(EntityPRooter.class)
public abstract class EntityPRooterMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries = null;

    @WrapOperation(
            method = "spawnLeemB",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z"),
            remap = false
    )
    private boolean srpmixins_sendSummonEntries(EntityParasiteBase entity, String[] mobList, int range, @Nullable EntityLivingBase target, Operation<Boolean> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        return original.call(entity, mobList, range, target);
    }
}
