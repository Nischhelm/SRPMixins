package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Random;

@Mixin(ParasiteEventWorld.class)
public class ReduceInfestedGrassSpawnrate {
    @WrapOperation(
            method = "spawnGenFeatureInfested",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 0),
            remap = false
    )
    private static int srpmixins_startPropagatingStagedInfestation(Random instance, int i, Operation<Integer> original) {
        return original.call(instance, (int) (i * SRPMixinsConfigHandler.deterrents.infestedGrassSpawnRateMultiplier));
    }
}