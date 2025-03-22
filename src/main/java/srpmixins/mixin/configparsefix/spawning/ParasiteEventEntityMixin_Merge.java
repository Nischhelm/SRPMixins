package srpmixins.mixin.configparsefix.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;
import srpmixins.util.ParaSpawnEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin_Merge {
    @Unique private static Map<Integer, String> srpmixins$mergeMap = null;
    @Unique private static List<String> srpmixins$mergeList = null;
    @Unique private static final String[] srpmixins$emptyList = {"",""};
    @Unique private static Integer srpmixins$selectedMergeIndex = null;
    @Unique private static String srpmixins$getMergedEntity(int index, boolean fromList){
        if(srpmixins$mergeMap == null){
            srpmixins$mergeMap = new HashMap<>();
            srpmixins$mergeList = new ArrayList<>();
            for(String s : SRPConfigSystems.mergeMobTable){
                String[] split = s.split(";");
                srpmixins$mergeMap.put(Integer.parseInt(split[1]), split[0]);
                srpmixins$mergeList.add(split[0]);
            }
        }
        return fromList ? srpmixins$mergeList.get(index) : srpmixins$mergeMap.get(index);
    }

    @WrapOperation(
            method = "merge",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original){
        return srpmixins$emptyList;
    }

    @Redirect(
            method = "merge",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;mergeMobTable:[Ljava/lang/String;", opcode = Opcodes.ARRAYLENGTH, args = "array=length", ordinal = 2),
            remap = false
    )
    private static int srpmixins_onlyLoopOnce(String[] array){
        return Math.min(array.length, 1);
    }

    @WrapOperation(
            method = "merge",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"),
            remap = false
    )
    private static int srpmixins_dontParseIndex(String s, Operation<Integer> original, @Local(argsOnly = true) int code){
        //If map has an entry for code, return code to write into index, otherwise return -999 to never pass the next check and instead do the random fallback from SRP
        if(srpmixins$getMergedEntity(code, false) != null){
            srpmixins$selectedMergeIndex = code;
            return code;
        }
        return -999;
    }

    @WrapOperation(
            method = "merge",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IZLjava/lang/String;)V"),
            remap = false
    )
    private static void srpmixins_sendSpawnEntries(EntityParasiteBase total, String[] parasite, int entityout, boolean max, String min, Operation<Void> original, @Local(ordinal = 1) int index){
        if(srpmixins$selectedMergeIndex != null) ParaSpawnEntry.setCurrentSpawnList(new ParaSpawnEntry(srpmixins$getMergedEntity(srpmixins$selectedMergeIndex, false), 1, 1));
        else ParaSpawnEntry.setCurrentSpawnList(new ParaSpawnEntry(srpmixins$getMergedEntity(index, true), 1, 1));

        original.call(total, parasite, entityout, max, min);
        srpmixins$selectedMergeIndex = null;
    }
}
