package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEvolutionLure.class)
public class CarcassAllSameLures {
    @Shadow(remap = false) @Final public static PropertyEnum<BlockEvolutionLure.EnumType> VARIANT;

    @Unique private BlockPos srpmixins$savedPosition;

    @Inject(
            method = "checkBlocks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")
    )
    private void srpmixins_savePosition(World worldIn, BlockPos pos, BlockEvolutionLure.EnumType t, CallbackInfoReturnable<Boolean> cir){
        this.srpmixins$savedPosition = pos;
    }
    
    @ModifyExpressionValue(
            method = "checkBlocks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getBlock()Lnet/minecraft/block/Block;"),
            remap = false
    )
    private Block srpmixins_checkLuresForType(Block original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockEvolutionLure.EnumType lureType) {
        if(srpmixins$savedPosition == null) return original;
        if (!world.getBlockState(srpmixins$savedPosition).getValue(VARIANT).equals(lureType)) {
            srpmixins$savedPosition = null;
            return Blocks.AIR;
        }
        return original;
    }
}
