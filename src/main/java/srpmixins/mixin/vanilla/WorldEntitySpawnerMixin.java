package srpmixins.mixin.vanilla;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ParasiteCreatureType;

@Mixin(WorldEntitySpawner.class)
public abstract class WorldEntitySpawnerMixin {
    @WrapOperation(
            method = "findChunksForSpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EnumCreatureType;getMaxNumberOfCreature()I")
    )
    private int srpmixins_setParasiteMobCap(EnumCreatureType instance, Operation<Integer> original, @Local EnumCreatureType type){
        if(type != ParasiteCreatureType.PARASITE) return original.call(instance);
        return SRPConfig.worldMobCapPlusPlayer;
    }

    @ModifyExpressionValue(
            method = "findChunksForSpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;countEntities(Lnet/minecraft/entity/EnumCreatureType;Z)I", remap = false)
    )
    private int srpmixins_addFlatParasiteMobCap(int original, @Local EnumCreatureType type){
        if(type != ParasiteCreatureType.PARASITE) return original;
        return original - SRPConfig.worldMobCap; //effectively increases mobcap per player by this flat amount by subtracting this from actual count
    }

    //TODO: vary mobcap depending on whatever
}
