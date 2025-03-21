package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.util.ParaSpawnEntry;

import java.util.List;

@Mixin(ParasiteEventEntity.class)
//This is so stupid
public abstract class ParasiteEventEntityMixin_SummonBlock {
    @Unique private static List<ParaSpawnEntry> srpmixins$currentSpawnList_summonBlock = null;
    @Unique private static final String[] srpmixins$emptyList_summonBlock = {"",""};
    
    @Inject(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;<init>()V"),
            remap = false
    )
    private static void srpmixins_getSpawnListValues(World world, String[] out, int range, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList_summonBlock = ParaSpawnEntry.getAndClearCurrentSpawnList();
    }

    @WrapOperation(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original){
        if(srpmixins$currentSpawnList_summonBlock == null) return original.call(instance, regex); //Default behavior
        return srpmixins$emptyList_summonBlock;
    }

    @WrapOperation(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;parseDouble(Ljava/lang/String;)D"),
            remap = false
    )
    private static double srpmixins_dontParseSummonChance(String s, Operation<Double> original, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList_summonBlock == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList_summonBlock.get(i).chance;
    }

    @ModifyArg(
            method = "spawnFromBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;<init>(Ljava/lang/String;)V"),
            remap = false
    )
    private static String srpmixins_dontParseSummonMobId(String resourceName, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList_summonBlock == null) return resourceName; //Default behavior
        return srpmixins$currentSpawnList_summonBlock.get(i).mobid;
    }

    @Inject(
            method = "spawnFromBlock",
            at = @At("RETURN"),
            remap = false
    )
    private static void srpmixins_removeCachedSpawnList(World world, String[] out, int range, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList_summonBlock = null;
    }
}
