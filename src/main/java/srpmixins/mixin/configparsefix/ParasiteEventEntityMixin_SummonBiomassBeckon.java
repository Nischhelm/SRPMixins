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
public abstract class ParasiteEventEntityMixin_SummonBiomassBeckon {
    @Unique private static List<ParaSpawnEntry> srpmixins$currentSpawnList_summonBeckon = null;
    @Unique private static final String[] srpmixins$emptyList_summonBeckon = {"","",""};
    
    @Inject(
            method = "spawnBiomassFromBeckon",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;<init>()V"),
            remap = false
    )
    private static void srpmixins_getSpawnListValues(EntityParasiteBase entityin, int stage, EntityLivingBase target, boolean payfather, String[] ground, String[] air, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList_summonBeckon = ParaSpawnEntry.getAndClearCurrentSpawnList();
    }

    @WrapOperation(
            method = "spawnBiomassFromBeckon",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original){
        if(srpmixins$currentSpawnList_summonBeckon == null) return original.call(instance, regex); //Default behavior
        return srpmixins$emptyList_summonBeckon;
    }

    @WrapOperation(
            method = "spawnBiomassFromBeckon",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;parseDouble(Ljava/lang/String;)D"),
            remap = false
    )
    private static double srpmixins_dontParseSummonChance(String s, Operation<Double> original, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList_summonBeckon == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList_summonBeckon.get(i).chance;
    }

    @WrapOperation(
            method = "spawnBiomassFromBeckon",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"),
            remap = false
    )
    private static int srpmixins_dontParseSummonPoints(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList_summonBeckon == null) return original.call(s); //Default behavior
        return srpmixins$currentSpawnList_summonBeckon.get(i).points;
    }

    @ModifyArg(
            method = "spawnBiomassFromBeckon",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/monster/EntityBiomass;setParasite(Ljava/lang/String;I)V"),
            remap = false
    )
    private static String srpmixins_dontParseSummonMobId(String resourceName, @Local(ordinal = 1) int i){
        if(srpmixins$currentSpawnList_summonBeckon == null) return resourceName; //Default behavior
        return srpmixins$currentSpawnList_summonBeckon.get(i).mobid;
    }

    @Inject(
            method = "spawnBiomassFromBeckon",
            at = @At("RETURN"),
            remap = false
    )
    private static void srpmixins_removeCachedSpawnList(EntityParasiteBase entityin, int stage, EntityLivingBase target, boolean payfather, String[] ground, String[] air, CallbackInfoReturnable<Boolean> cir){
        srpmixins$currentSpawnList_summonBeckon = null;
    }
}
