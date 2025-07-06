package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasite;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class ColonyLockFix_ParaBiome {
    @Inject(
            method = "checkColoLock",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    private static void srpmixins_checkBiomeEarly(int in, SRPWorldData data, EntityParasiteBase para, CallbackInfoReturnable<Boolean> cir){
        //Issue being fixed: if preeValuesBiome is on true, spawns in para biome would NEVER allow preeminents no matter how many colony points
        if (para.world.getBiome(para.getPosition()) instanceof BiomeParasite && !SRPConfigWorld.preeValuesBiome)
            cir.setReturnValue(false);
    }

    @WrapOperation(
            method = "checkColoLock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;")
    )
    private static Biome srpmixins_lateBiomeCheckFails(World instance, BlockPos pos, Operation<Biome> original){
        return Biomes.PLAINS; //any biome that isn't para biome to fail the original check
    }
}
