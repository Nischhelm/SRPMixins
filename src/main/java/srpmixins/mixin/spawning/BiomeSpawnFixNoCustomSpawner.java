package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasiteBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class BiomeSpawnFixNoCustomSpawner {
    @WrapOperation(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning$DimensionHandler;canSpawninPhase(IILcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;)Z"),
            remap = false
    )
    private static boolean srpmixins_onlyCheckIfNotBiome(int evPhase, int levelUD, EntityParasiteBase parasite, Operation<Boolean> original){
        Biome currBiome = parasite.world.getBiome(parasite.getPosition());
        return currBiome instanceof BiomeParasiteBase || original.call(evPhase, levelUD, parasite);
    }
}
