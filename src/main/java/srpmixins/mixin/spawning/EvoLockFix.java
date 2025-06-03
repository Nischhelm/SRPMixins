package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class EvoLockFix {
    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning$DimensionHandler;checkEvoLock(ILcom/dhanantry/scapeandrunparasites/world/SRPSaveData;)Z"),
            remap = false
    )
    private static boolean srpmixins_onlyCheckIfActivated(int in, SRPSaveData data, Operation<Boolean> original){
        return SRPConfigSystems.useEvolution && original.call(in, data);
    }
}
