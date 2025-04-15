package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class ColonyLockFix {
    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning$DimensionHandler;checkColoLock(ILcom/dhanantry/scapeandrunparasites/world/SRPWorldData;Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;)Z"),
            remap = false
    )
    private static boolean srpmixins_onlyCheckIfActivated(int i, SRPWorldData in, EntityParasiteBase data, Operation<Boolean> original){
        return SRPConfigWorld.coloniesActivated && original.call(i, in, data);
    }
}
