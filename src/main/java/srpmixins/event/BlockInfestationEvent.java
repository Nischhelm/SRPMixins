package srpmixins.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import srpmixins.rules.BlockTransformationRule;

/**
 * This event will only be fired
 * if BiomeSpreadOverhaul is enabled (for para biome)
 * or if InfestationOverhaul is enabled (for beckon infestation).
 * It is not fired when reverting infestation.
 */
@Cancelable
public class BlockInfestationEvent extends BlockEvent {
    public EnumFacing facing = null;
    public final boolean isParaBiome;
    public IBlockState resultState;
    public final int stage;
    public boolean generatesAbove, generatesBelow, increasesPoints;


    public boolean canceledFully = false;
    public boolean isCanceledFully(){ return canceledFully;}
    public void cancelFully(){ this.canceledFully = true; }


    /**
     * This event is used when blocks transform due to parasite biome or beckon infestation spreading.
     * @param world the world it happens in
     * @param pos the block position it happens at
     * @param resultState the blockstate the current block turns into (use world.getBlockstate to get the current=previous blockstate)
     * @param isParaBiomeInfestation whether this event was posted by para biome (true) or beckon infestation (false)
     * @param generatesAbove whether the block infestation should try to generate features (usually vegetation) above the infested block
     * @param generatesBelow whether the block infestation should try to generate features (usually vegetation) below the infested block
     * @param increasesPoints whether this infestation should increase evolution points
     * use cancelFully to not only cancel the current infestation but the whole set of 6 (to 27) blocks currently being infested
     */
    public BlockInfestationEvent(World world, BlockPos pos, IBlockState startState, IBlockState resultState, boolean isParaBiomeInfestation, int stage, boolean generatesAbove, boolean generatesBelow, boolean increasesPoints) {
        super(world, pos, startState);
        this.resultState = resultState;
        this.isParaBiome = isParaBiomeInfestation;
        this.stage = stage;
        this.generatesAbove = generatesAbove;
        this.generatesBelow = generatesBelow;
        this.increasesPoints = increasesPoints;
    }

    public BlockInfestationEvent(World world, BlockPos pos, IBlockState startState, IBlockState resultState, boolean isParaBiome, BlockTransformationRule rule) {
        this(world, pos, startState, resultState, isParaBiome, 0, rule.getGeneratesAbove(), rule.getGeneratesBelow(), rule.getAddsPoints());
    }

    public BlockInfestationEvent setFacing(EnumFacing facing) {
        this.facing = facing;
        return this;
    }

    public IBlockState getResultState(){
        return this.resultState;
    }
}
