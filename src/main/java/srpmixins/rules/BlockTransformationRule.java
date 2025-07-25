package srpmixins.rules;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedOre;
import com.dhanantry.scapeandrunparasites.block.BlockParasiteRubble;
import com.dhanantry.scapeandrunparasites.block.BlockParasiteStain;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class BlockTransformationRule {
    private static final Map<Material, List<BlockTransformationRule>> allRules = new HashMap<>();

    private final Predicate<Block> predicateBlock;

    private final IBlockState resultState;
    private boolean addsPoints = true;
    private boolean doGenAbove = false;
    private boolean doGenBelow = false;
    private boolean skipsHardnessCheck = false;

    public BlockTransformationRule(Material material, Predicate<Block> predicateBlock, IBlockState resultState) {
        this.predicateBlock = predicateBlock;
        this.resultState = resultState;

        allRules.computeIfAbsent(material, key -> new ArrayList<>()).add(this);
    }

    public BlockTransformationRule setNoPoints() {
        this.addsPoints = false;
        return this;
    }

    public BlockTransformationRule setGeneratesAbove() {
        this.doGenAbove = true;
        return this;
    }

    public BlockTransformationRule setGeneratesBelow() {
        this.doGenBelow = true;
        return this;
    }

    public BlockTransformationRule setSkipsHardnessCheck() {
        this.skipsHardnessCheck = true;
        return this;
    }

    public boolean getGeneratesAbove() {
        return this.doGenAbove;
    }

    public boolean getGeneratesBelow() {
        return this.doGenBelow;
    }

    public boolean getAddsPoints() {
        return this.addsPoints;
    }

    public boolean getSkipHardnessCheck() {
        return this.skipsHardnessCheck;
    }

    @Nullable
    public IBlockState getResultState() {
        return this.resultState;
    }

    public boolean isApplicable(Block block) {
        return this.predicateBlock == null || this.predicateBlock.test(block);
    }

    @Nullable
    public static BlockTransformationRule getFirstApplicableRule(Material material, Block blockToTest){
        if(allRules.isEmpty()){
            //Order inside each material group matters (for result and performance)

            /*GROUND*/	new BlockTransformationRule(Material.GROUND, null, SRPBlocks.ParasiteStain.getDefaultState()).setGeneratesBelow().setGeneratesAbove();
            /*GRASS*/	new BlockTransformationRule(Material.GRASS, null, SRPBlocks.ParasiteStain.getDefaultState()).setGeneratesBelow().setGeneratesAbove();
            /*SAND*/	new BlockTransformationRule(Material.SAND, null, SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.MUD)).setGeneratesBelow().setGeneratesAbove();
            /*PLANTS*/	new BlockTransformationRule(Material.PLANTS, null, SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.FLESH));
            /*IRON*/	new BlockTransformationRule(Material.IRON, null, SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.METAL));
            /*PACKED_ICE*/	new BlockTransformationRule(Material.PACKED_ICE, null, SRPBlocks.BloodyIce.getDefaultState());

            /*ICE*/	new BlockTransformationRule(Material.ICE, block -> block == SRPBlocks.BloodyIce, null).setNoPoints().setSkipsHardnessCheck();
            /*ICE*/	new BlockTransformationRule(Material.ICE, null, SRPBlocks.BloodyIce.getDefaultState()).setSkipsHardnessCheck();

            /*STONE*/	new BlockTransformationRule(Material.ROCK, block -> block instanceof BlockStone, SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.STONE)).setGeneratesAbove();
            /*COAL_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.COAL_ORE, SRPBlocks.InfestedOre.getDefaultState()).setGeneratesBelow();
            /*IRON_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.IRON_ORE, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.IRO)).setGeneratesBelow();
            /*DIA_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.DIAMOND_ORE, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.DIA)).setGeneratesBelow();
            /*EME_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.EMERALD_ORE, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.EME)).setGeneratesBelow();
            /*GOLD_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.GOLD_ORE, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.GOL)).setGeneratesBelow();
            /*LAPIS_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.LAPIS_ORE, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.LAP)).setGeneratesBelow();
            /*REDS_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block == Blocks.REDSTONE_ORE, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.RED)).setGeneratesBelow();
            /*OTHER_ORE*/	new BlockTransformationRule(Material.ROCK, block -> block instanceof BlockOre, SRPBlocks.InfestedOre.getDefaultState().withProperty(BlockInfestedOre.VARIANT, BlockInfestedOre.EnumType.UN)).setGeneratesBelow();
            /*OBSIDIAN*/	new BlockTransformationRule(Material.ROCK, block -> block instanceof BlockObsidian, SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.OBSIDIAN)).setGeneratesBelow().setGeneratesAbove();
            /*NOCORE*/	new BlockTransformationRule(Material.ROCK, block -> block == SRPBlocks.BiomeHeart || block == SRPBlocks.ColonyHeart, null).setNoPoints();
            /*BRICK*/	new BlockTransformationRule(Material.ROCK, block -> block.getRegistryName().toString().contains("brick"), SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.BRICKS)).setGeneratesBelow().setGeneratesAbove();
            /*OTHERROCK*/	new BlockTransformationRule(Material.ROCK, null, SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.STONE)).setGeneratesBelow().setGeneratesAbove();

            /*INFESTED_TRUNK*/	new BlockTransformationRule(Material.WOOD, block -> block == SRPBlocks.InfestedTrunk, SRPBlocks.ParasiteTrunk.getDefaultState()).setNoPoints();
            /*MUSHROOM*/	new BlockTransformationRule(Material.WOOD, block -> block.getRegistryName().toString().contains("mushroom"), SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.FUNGUS));
            /*OTHERWOOD*/	new BlockTransformationRule(Material.WOOD, null, SRPBlocks.ParasiteRubble.getDefaultState().withProperty(BlockParasiteRubble.VARIANT, BlockParasiteRubble.EnumType.WOOD));
        }

        for(BlockTransformationRule rule : allRules.getOrDefault(material, Collections.emptyList()))
            if (rule.isApplicable(blockToTest)) return rule;
        return null;
    }
}
