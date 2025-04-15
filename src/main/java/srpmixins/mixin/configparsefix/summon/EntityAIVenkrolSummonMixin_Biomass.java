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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(EntityAIVenkrolSummon.class)
public abstract class EntityAIVenkrolSummonMixin_Biomass {
    @Unique private static final Map<Integer, List<ParaSpawnEntry>> srpmixins$spawnEntryMap = new HashMap<>();

    @WrapOperation(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnBiomassFromBeckon(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;ILnet/minecraft/entity/EntityLivingBase;Z[Ljava/lang/String;[Ljava/lang/String;)Z"),
            remap = false
    )
    private boolean srpmixins_sendSummonEntries(EntityParasiteBase entityin, int stage, EntityLivingBase target, boolean countSummons, String[] ground, String[] air, Operation<Boolean> original){
        boolean isGround =  target.posY - entityin.posY < 3.0;
        int identifier = stage * (isGround ? 1 : 2); //to map spawn entry lists to stage x ground/air
        if(!srpmixins$spawnEntryMap.containsKey(identifier))
            srpmixins$spawnEntryMap.put(identifier, ParaSpawnEntry.parseMobList(isGround ? ground : air, false));

        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntryMap.get(identifier));
        return original.call(entityin, stage, target, countSummons, ground, air);
    }
}
