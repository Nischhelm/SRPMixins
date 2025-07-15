package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPStationaryArchitect.class)
public class InfestationReversionToggle {
    @WrapOperation(
            method = "freeDead",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z")
    )
    private boolean srpmixins_disableBlockReversion(World instance, BlockPos pos, IBlockState state, Operation<Boolean> original){
        return false;
    }

    @WrapWithCondition(
            method = "freeDead",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V")
    )
    private boolean srpmixins_disableBlockReversion(World instance, BlockPos pos, Block blockIn, int delay, int priority){
        return false;
    }
}
