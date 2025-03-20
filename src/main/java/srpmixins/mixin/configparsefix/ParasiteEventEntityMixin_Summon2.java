package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
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
public abstract class ParasiteEventEntityMixin_Summon2 {
    @Unique private static List<ParaSpawnEntry> srpmixins$currentSpawnList = null;
    @Unique private static final String[] srpmixins$emptyList = {"","",""};
    
    @Inject(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;<init>()V"),
            remap = false
    )
    private static void srpmixins_getSpawnListValues(EntityParasiteBase entityin, String[] out, int range, EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList = ParaSpawnEntry.getAndClearCurrentSpawnList();
    }

    @WrapOperation(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original){
        if(srpmixins$currentSpawnList == null) return original.call(instance, regex); //Default behavior
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;parseDouble(Ljava/lang/String;)D"),
            remap = false
    )
    private static double srpmixins_dontParseSummonChance(String s, Operation<Double> original, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList.get(i).chance;
    }

    @WrapOperation(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"),
            remap = false
    )
    private static int srpmixins_dontParseSummonPoints(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList.get(i).points;
    }

    @ModifyArg(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;<init>(Ljava/lang/String;)V")
    )
    private static String srpmixins_dontParseSummonMobId(String resourceName, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList == null) return resourceName; //Default behavior
        return srpmixins$currentSpawnList.get(i).mobid;
    }

    @Inject(
            method = "SummonM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;ILnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At("RETURN"),
            remap = false
    )
    private static void srpmixins_removeCachedSpawnList(EntityParasiteBase entityin, String[] out, int range, EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList = null;
    }
}
