package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(BlockEvolutionLure.class)
public class CarcassAllSameLures {
    @Shadow(remap = false) @Final public static PropertyEnum<BlockEvolutionLure.EnumType> VARIANT;

    /**
     * @author Nischhelm
     * @reason stupid and dumb
     */
    @Overwrite(remap = false)
    private boolean checkBlocks(World worldIn, BlockPos pos, BlockEvolutionLure.EnumType lureType) {
        boolean cornersAreSameVariant = true;
        boolean cornersAreLures = true;
        for (int xShift = -3; xShift <= 3; xShift += 6) {
            for (int zShift = -3; zShift <= 3; zShift += 6) {
                IBlockState blockComp = worldIn.getBlockState(pos.add(xShift, 0, zShift));
                if (blockComp.getBlock() != SRPBlocks.evolutionLure) {
                    cornersAreLures = false;
                    break;
                }
                if (!blockComp.getValue(VARIANT).equals(lureType)) {
                    cornersAreSameVariant = false;
                    break;
                }
            }
        }
        return cornersAreLures && (!SRPMixinsConfigHandler.lures.forceCarcassSameLureVariant || cornersAreSameVariant);
    }
}
