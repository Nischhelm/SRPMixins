package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.util.configparse.ParaSpawnEntry;

import java.util.List;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin_Summon {
    @Unique private static List<ParaSpawnEntry> srpmixins$currentSpawnList_summonM1 = null;
    @Unique private static final String[] srpmixins$emptyList_summonM1 = {"","",""};
    
    @Inject(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;<init>()V"),
            remap = false
    )
    private static void srpmixins_getSpawnListValues(EntityParasiteBase entityin, String[] out, int range, double tx, double ty, double tz, EntityLivingBase target, boolean pod, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList_summonM1 = ParaSpawnEntry.getAndClearCurrentSpawnList();
    }

    @WrapOperation(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original){
        if(srpmixins$currentSpawnList_summonM1 == null) return original.call(instance, regex); //Default behavior
        return srpmixins$emptyList_summonM1;
    }

    @WrapOperation(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;parseDouble(Ljava/lang/String;)D"),
            remap = false
    )
    private static double srpmixins_dontParseSummonChance(String s, Operation<Double> original, @Local(ordinal = 1) int i, @Share("index") LocalIntRef index){
        index.set(i);
        if(srpmixins$currentSpawnList_summonM1 == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList_summonM1.get(i).chance;
    }

    @WrapOperation(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"),
            remap = false
    )
    private static int srpmixins_dontParseSummonPoints(String s, Operation<Integer> original, @Share("index") LocalIntRef index){
        if(srpmixins$currentSpawnList_summonM1 == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList_summonM1.get(index.get()).points;
    }

    @ModifyArg(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;<init>(Ljava/lang/String;)V")
    )
    private static String srpmixins_dontParseSummonMobId(String resourceName, @Share("index") LocalIntRef index){
        if(srpmixins$currentSpawnList_summonM1 == null) return resourceName; //Default behavior
        return srpmixins$currentSpawnList_summonM1.get(index.get()).mobid;
    }

    @Inject(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IDDDLnet/minecraft/entity/EntityLivingBase;Z)Z",
            at = @At("RETURN"),
            remap = false
    )
    private static void srpmixins_removeCachedSpawnList(EntityParasiteBase entityin, String[] out, int range, double tx, double ty, double tz, EntityLivingBase target, boolean pod, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList_summonM1 = null;
    }
}
