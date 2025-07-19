package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.block.IMetaName;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.BlockTransformationRule;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mixin(ParasiteEventWorld.class)
public abstract class BiomeSpreadOverhaul {
    @Shadow(remap = false) public static void spawnGenFeatureParasite(World worldIn, BlockPos pos, Random rand) {}
    @Shadow(remap = false) public static void spawnGenRoofParasite(World worldIn, BlockPos pos, Random rand) {}
    @Shadow(remap = false) public static int blockParasiteCount;

    /**
     * @author Nischhelm
     * @reason stupid and laggy
     */
    @Overwrite(remap = false)
    public static void spreadBiomeBlockStain(World worldIn, BlockPos startPos, Random rand) {
        if(SRPMixinsConfigHandler.parabiome.fixBiomeSpreadingLimit && blockParasiteCount > SRPConfig.BlockParasiteLimit) return;

        int convertedCount = 0;

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (EnumFacing facing : EnumFacing.values()) {
            blockPos.setPos(startPos).move(facing);
            IBlockState state = worldIn.getBlockState(blockPos);
            Material material = state.getMaterial();
            if (material == Material.AIR) continue;

            Block block = state.getBlock();
            if (block instanceof IMetaName) continue;

            if (block.isWood(worldIn, blockPos) || block == SRPBlocks.InfestedStain) {
                ++convertedCount;
                worldIn.setBlockState(blockPos, SRPBlocks.ParasiteTrunk.getDefaultState());
                continue;
            }

            BlockTransformationRule rule = BlockTransformationRule.getFirstApplicableRule(material, block);
            if(rule == null) continue;
            if(rule.getResultState() == null) continue;

            if(!rule.getSkipHardnessCheck() && srpmixins$blockIsBlacklistedForBiome(worldIn, blockPos, block, state)) continue;

            worldIn.setBlockState(blockPos, rule.getResultState());
            if(rule.getGeneratesAbove()) spawnGenFeatureParasite(worldIn, blockPos.up(), rand);
            if(rule.getGeneratesBelow()) spawnGenRoofParasite(worldIn, blockPos.down(), rand);
            if(rule.getAddsPoints()) ++convertedCount;
        }

        if (convertedCount != 0) {
            blockParasiteCount += convertedCount;

            if (SRPConfigSystems.useEvolution){
                SRPSaveData data = SRPSaveDataInterface.get(worldIn, null, startPos);
                int lock = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
                if (lock < 0 || data.getEvolutionPhase(worldIn.provider.getDimension()) >= lock)
                    data.setTotalKills(worldIn.provider.getDimension(), SRPConfigSystems.valueBlock * convertedCount, true, worldIn, true);
            }
        }
    }

    @Unique private static final Set<Block> srpmixins$listedBlocks = new HashSet<>();

    @Unique
    private static boolean srpmixins$blockIsBlacklistedForBiome(World worldIn, BlockPos pos, Block block, IBlockState state) {
        float hardness = state.getBlockHardness(worldIn, pos);
        if(hardness < 0.0F || hardness > SRPConfigWorld.biomeBlockIMaxH) return true;

        if (!state.isFullCube() || block.isPassable(worldIn, pos) ||
                block instanceof BlockBreakable ||
                block instanceof BlockContainer ||
                block instanceof BlockHorizontal ||
                block instanceof BlockTNT ||
                block instanceof IPlantable) return true;
        else {
            if(srpmixins$listedBlocks.contains(block)) return !SRPConfigWorld.blockBBiomeListWhite;
            ResourceLocation loc = block.getRegistryName();
            if(loc == null) return SRPConfigSystems.blockBListWhite;
            String blockName = loc.toString();
            boolean isInList = Arrays.stream(SRPConfigWorld.blockBBiomeList).anyMatch(blockName::contains);
            if(isInList) srpmixins$listedBlocks.add(block);

            return isInList != SRPConfigWorld.blockBBiomeListWhite;
        }
    }

    /**
     * @author Nischhelm
     * @reason still stupid and bad for performance
     */
    @Overwrite(remap = false)
    public static void spreadBiomeBlockTrunk(World worldIn, BlockPos startPos, Random rand) {
        int convertedCount = 0;
        for (BlockPos.MutableBlockPos blockPos : BlockPos.getAllInBoxMutable(startPos.add(-1, -1, -1), startPos.add(1,1,1))) {
            IBlockState lookingState = worldIn.getBlockState(blockPos);
            Block lookingBlock = lookingState.getBlock();
            if(lookingBlock instanceof IMetaName) continue;

            if (lookingBlock.isWood(worldIn, blockPos)) {
                worldIn.setBlockState(blockPos, SRPBlocks.ParasiteTrunk.getDefaultState());
                convertedCount++;
            }
        }

        if(convertedCount != 0) {
            blockParasiteCount += convertedCount;
            if (SRPConfigSystems.useEvolution) {
                SRPSaveData data = SRPSaveDataInterface.get(worldIn, null, startPos);
                int lock = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
                if (lock < 0 || data.getEvolutionPhase(worldIn.provider.getDimension()) >= lock)
                    data.setTotalKills(worldIn.provider.getDimension(), SRPConfigSystems.valueBlock * convertedCount, true, worldIn, true);
            }
        }
    }
}