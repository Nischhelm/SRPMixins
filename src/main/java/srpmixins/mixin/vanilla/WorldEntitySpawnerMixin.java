package srpmixins.mixin.vanilla;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.util.ParasiteCreatureType;

@Debug(export = true)
@Mixin(WorldEntitySpawner.class)
public abstract class WorldEntitySpawnerMixin {
    @ModifyVariable(
            method = "findChunksForSpawning",
            at = @At("STORE"),
            name = "k4"
    )
    private int srpmixins_addFlatParasiteMobCap(int original, @Local EnumCreatureType type){
        if(type != ParasiteCreatureType.PARASITE) return original;
        return original - SRPConfig.worldMobCap; //effectively increases mobcap per player by this flat amount by subtracting this from actual count
        //actualCount <= worldMobCap + ( worldMobCapPlusPlayer * chunksCounted / magicNumber )
    }

    @ModifyVariable(
            method = "findChunksForSpawning",
            at = @At("STORE"),
            name = "l4"
    )
    private int srpmixins_setParasiteMobCapPerPlayer(int original, @Local EnumCreatureType type){
        if(type != ParasiteCreatureType.PARASITE) return original;
        return original * SRPConfig.worldMobCapPlusPlayer;
    }

    //TODO: vary mobcap depending on whatever
}
