package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanSpawn;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPFeral;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class MinFeralisations {
    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanSpawn;canSpawnByIDData()I"),
            remap = false
    )
    private static int srpmixins_minFeralisations(EntityCanSpawn instance, Operation<Integer> original, @Local EntityParasiteBase parasite){
        if(parasite instanceof EntityPFeral)
            return SRPMixinsConfigProvider.minFeralisations.getOrDefault(parasite.getParasiteIDRegister(), original.call(instance));
        return original.call(instance);
    }

    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getNumberIDDataSpawn(I)I"),
            remap = false
    )
    private static int srpmixins_minFeralisations(SRPSaveData instance, int i, Operation<Integer> original, @Local EntityParasiteBase parasite){
        //Overwrites the behavior where for ferals it would check how many assims got created
        if(parasite instanceof EntityPFeral && SRPMixinsConfigProvider.minFeralisations.containsKey(parasite.getParasiteIDRegister()))
            return original.call(instance, parasite.getParasiteIDRegister());
        return original.call(instance, i);
    }
}
