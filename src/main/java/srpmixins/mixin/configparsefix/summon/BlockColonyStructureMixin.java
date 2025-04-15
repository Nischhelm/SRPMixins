package srpmixins.mixin.configparsefix.summon;

import com.dhanantry.scapeandrunparasites.block.BlockColonyStructure;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.configparse.ParaSpawnEntry;

import java.util.List;

@Mixin(BlockColonyStructure.class)
public abstract class BlockColonyStructureMixin {
    @Unique private static List<ParaSpawnEntry> srpmixins$spawnEntries = null;

    @WrapOperation(
            method = "updateTick",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnFromBlock(Lnet/minecraft/world/World;[Ljava/lang/String;ILnet/minecraft/util/math/BlockPos;)Z"),
            remap = false
    )
    private boolean srpmixins_sendSummonEntries_Ticked(World world, String[] mobList, int range, BlockPos pos, Operation<Boolean> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        return original.call(world, mobList, range, pos);
    }

    @WrapOperation(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnFromBlock(Lnet/minecraft/world/World;[Ljava/lang/String;ILnet/minecraft/util/math/BlockPos;)Z"),
            remap = false
    )
    private boolean srpmixins_sendSummonEntries_Activated(World world, String[] mobList, int range, BlockPos pos, Operation<Boolean> original){
        if(srpmixins$spawnEntries == null) srpmixins$spawnEntries = ParaSpawnEntry.parseMobList(mobList, false);
        ParaSpawnEntry.setCurrentSpawnList(srpmixins$spawnEntries);
        return original.call(world, mobList, range, pos);
    }
}
