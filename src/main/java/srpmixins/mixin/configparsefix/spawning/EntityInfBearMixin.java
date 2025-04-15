package srpmixins.mixin.configparsefix.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfBear;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.configparse.ParaSpawnEntry;

import java.util.List;

@Mixin(EntityInfBear.class)
public abstract class EntityInfBearMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries = null;

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IZLjava/lang/String;)V"),
            remap = false
    )
    private void srpmixins_sendSpawnEntries(EntityParasiteBase entity, String[] mobList, int particle, boolean cannotDespawn, String name, Operation<Void> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, true);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        original.call(entity, mobList, particle, cannotDespawn, name);
    }
}
