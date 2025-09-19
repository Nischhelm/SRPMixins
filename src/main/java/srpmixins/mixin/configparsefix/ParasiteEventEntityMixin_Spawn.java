package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.configparse.ParaSpawnEntry;

import java.util.List;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin_Spawn {
    @Unique private static final String[] srpmixins$emptyList = {"","0","0"};

    @Inject(
            method = "spawnM",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;<init>()V"),
            remap = false
    )
    private static void srpmixins_getSpawnListValues(EntityParasiteBase entityin, String[] out, int particle, boolean cannotDespawn, String name, CallbackInfo ci, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        spawnList.set(ParaSpawnEntry.getAndClearCurrentSpawnList());
    }

    @WrapOperation(
            method = "spawnM",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        if(spawnList.get() == null) return original.call(instance, regex); //Default behavior
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "spawnM",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private static int srpmixins_dontParseSpawnMaxCount(String s, Operation<Integer> original, @Local(ordinal = 2) int i, @Share("index") LocalIntRef index, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        index.set(i);
        if(spawnList.get() == null) return original.call(s); //Default behavior
        return spawnList.get().get(i).maxCount;
    }

    @WrapOperation(
            method = "spawnM",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private static int srpmixins_dontParseSpawnMinCount(String s, Operation<Integer> original, @Share("index") LocalIntRef index, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        if(spawnList.get() == null) return original.call(s); //Default behavior
        return spawnList.get().get(index.get()).minCount;
    }

    @ModifyArg(
            method = "spawnM",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;<init>(Ljava/lang/String;)V")
    )
    private static String srpmixins_dontParseSpawnMobId(String resourceName, @Share("index") LocalIntRef index, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        if(spawnList.get() == null) return resourceName; //Default behavior
        return spawnList.get().get(index.get()).mobid;
    }
}
