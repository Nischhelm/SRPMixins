package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRubble;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedStain;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityThrowableAntiInfestedBlock;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityThrowableAntiInfestedBlock.class)
public abstract class QuenchRemovesRemains extends EntityThrowable {
    public QuenchRemovesRemains(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "onImpact",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z")
    )
    private boolean srpmixins_includeOtherBlocks(boolean original, RayTraceResult result, @Cancellable CallbackInfo ci){
        if(original) return true; //clientside code isnt touched

        int rangeY = 5;
        int range = 7;
        int dY = MathHelper.floor(this.posY + 0.1D - this.getPosition().getY()); //adds +1 to y coord if precise pos is close to clipping to next block above (idk why, just copying SRP here)
        
        for(BlockPos.MutableBlockPos blockpos : BlockPos.getAllInBoxMutable(
                this.getPosition().add(range, rangeY + dY, range),
                this.getPosition().add(-range, -rangeY + dY, -range))
        ) {
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (block == SRPBlocks.InfestedStain) {
                this.world.setBlockState(blockpos, block.getDefaultState().withProperty(BlockInfestedStain.STAGE, 5));
                this.world.updateBlockTick(blockpos, block, 40, 5);
            } else if(block == SRPBlocks.InfestedRubble) {
                this.world.setBlockState(blockpos, block.getDefaultState().withProperty(BlockInfestedRubble.STAGE, 5));
                this.world.updateBlockTick(blockpos, block, 40, 5);
            } else if(block == SRPBlocks.InfestRemain) {
                this.world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
            }
        }
        setDead();
        ci.cancel();
        return false; //return original;
    }
}
