package srpmixins.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import srpmixins.rules.BlockTransformationRule;

@Cancelable
//This event will only be fired
// - if BiomeSpreadOverhaul is enabled (for para biome)
// - or if InfestationOverhaul is enabled (for beckon infestation).
// It is not fired when reverting infestation.
public class BlockInfestationEvent extends BlockEvent {
    public final boolean isParaBiome;
    public IBlockState resultState;
    public boolean generatesAbove, generatesBelow, increasesPoints;

    /**
     * This event is used when blocks transform due to parasite biome or beckon infestation spreading.
     * @param world the world it happens in
     * @param pos the block position it happens at
     * @param resultState the blockstate the current block turns into (use world.getBlockstate to get the current=previous blockstate)
     * @param isParaBiomeInfestation whether this event was posted by para biome (true) or beckon infestation (false)
     * @param generatesAbove whether the block infestation should try to generate features (usually vegetation) above the infested block
     * @param generatesBelow whether the block infestation should try to generate features (usually vegetation) below the infested block
     * @param increasesPoints whether this infestation should increase evolution points
     */
    public BlockInfestationEvent(World world, BlockPos pos, IBlockState resultState, boolean isParaBiomeInfestation, boolean generatesAbove, boolean generatesBelow, boolean increasesPoints) {
        super(world, pos, resultState);
        this.resultState = resultState;
        this.isParaBiome = isParaBiomeInfestation;
        this.generatesAbove = generatesAbove;
        this.generatesBelow = generatesBelow;
        this.increasesPoints = increasesPoints;
    }

    public BlockInfestationEvent(World world, BlockPos pos, IBlockState resultState, boolean isParaBiome, BlockTransformationRule rule) {
        this(world, pos, resultState, isParaBiome, rule.getGeneratesAbove(), rule.getGeneratesBelow(), rule.getAddsPoints());
    }

    public void setResultState(IBlockState newResultState){
        this.resultState = newResultState;
    }

    @Override
    public IBlockState getState(){
        return  this.resultState;
    }
}
