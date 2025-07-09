package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Collections;
import java.util.List;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class CheckSpawnHandlerDisable {
    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;")
    )
    private static List<Entity> srpmixins_disableMobCapCheck(List<Entity> original){
        return Collections.emptyList(); //Don't run dimension blacklist check
    }

    @ModifyExpressionValue(
            method = "onSpawn",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
                    to = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;blackListedDimensionsWhite:Z", ordinal = 0)
            ),
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    private static boolean srpmixins_disableDimensionBlacklistCheck(boolean original){
        return true; //Don't run dimension blacklist check
    }

    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning$DimensionHandler;canSpawninPhase(BLcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;)Z"),
            remap = false
    )
    private static boolean srpmixins_disablePhaseIdCheck(byte evPhase, EntityParasiteBase parasite, Operation<Boolean> original){
        return true; //Don't run id check
    }

    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning$DimensionHandler;checkEvoLock(ILcom/dhanantry/scapeandrunparasites/world/SRPSaveData;)Z"),
            remap = false
    )
    private static boolean srpmixins_disableEvoLockCheck(int in, SRPSaveData data, Operation<Boolean> original){
        return false; //Don't run evo lock check
    }

    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning$DimensionHandler;checkColoLock(ILcom/dhanantry/scapeandrunparasites/world/SRPWorldData;Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;)Z"),
            remap = false
    )
    private static boolean srpmixins_disableColoLockCheck(int i, SRPWorldData in, EntityParasiteBase data, Operation<Boolean> original){
        return false; //Don't run colo lock check
    }
}
