package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEvolutionLure.class)
public class CarcassAllSameLures {
    @Shadow(remap = false) @Final public static PropertyEnum<BlockEvolutionLure.EnumType> VARIANT;

    @WrapOperation(
            method = "checkBlocks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")
    )
    private IBlockState srpmixins_checkIfCorrectType(World world, BlockPos pos, Operation<IBlockState> original, @Local(argsOnly=true) BlockEvolutionLure.EnumType centerType){
        IBlockState state = original.call(world, pos);
        if(state.getBlock() == SRPBlocks.evolutionLure) {
            BlockEvolutionLure.EnumType compareType = state.getValue(VARIANT);
            if(compareType != centerType) return Blocks.AIR.getDefaultState();
        }
        return state;
    }
}
