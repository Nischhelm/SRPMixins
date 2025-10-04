package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.util.configparse.ParaSpawnEntry;

import java.util.List;

@Mixin(ParasiteEventEntity.class)
//This is so stupid
public abstract class ParasiteEventEntityMixin_SummonBlock {
    @Unique @Final private static final String[] srpmixins$emptyList = {"","0","0"};
    
    @Inject(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;<init>()V"),
            remap = false
    )
    private static void srpmixins_getSpawnListValues(World world, String[] out, int range, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        spawnList.set(ParaSpawnEntry.getAndClearCurrentSpawnList());
    }

    @WrapOperation(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        if(spawnList.get() == null) return original.call(instance, regex); //Default behavior
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;parseDouble(Ljava/lang/String;)D"),
            remap = false
    )
    private static double srpmixins_dontParseSummonChance(String s, Operation<Double> original, @Local(ordinal = 1) int i, @Share("index") LocalIntRef index, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        index.set(i);
        if(spawnList.get() == null) return original.call(s); //Default behavior
        return spawnList.get().get(i).chance;
    }

    @ModifyArg(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;<init>(Ljava/lang/String;)V"),
            remap = false
    )
    private static String srpmixins_dontParseSummonMobId(String resourceName, @Share("index") LocalIntRef index, @Share("spawnList") LocalRef<List<ParaSpawnEntry>> spawnList){
        if(spawnList.get() == null) return resourceName; //Default behavior
        return spawnList.get().get(index.get()).mobid;
    }
}
