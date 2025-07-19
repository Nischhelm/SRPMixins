package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.block.BlockBase;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedRubble;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedStain;
import com.dhanantry.scapeandrunparasites.block.IMetaName;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrol;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.*;

@Mixin(ParasiteEventWorld.class)
public abstract class InfestationOverhaul {
    @Shadow(remap = false) public static void spawnGenFeatureInfested(World worldIn, BlockPos pos, Random rand) {}
    @Shadow(remap = false) public static void spawnGenRoofInfested(World worldIn, BlockPos pos, Random rand) {}
    @Shadow(remap = false) public static int blockInfestedCount;

    /**
     * @author Nischhelm
     * @reason stupid, stupid, stupid
     */
    @Overwrite(remap = false)
    public static void canInfestBlock(World worldIn, BlockPos startPos, Random rand, int beckonStage, boolean alwaysTrue) {
        if(beckonStage >= 4){ //stage 4 never actually happens
            infestationReversionLogic(worldIn, startPos, beckonStage);
            return;
        }

        if (blockInfestedCount > SRPConfig.BlockInfestedLimit) return;
        if (worldIn.getDifficulty() == EnumDifficulty.PEACEFUL) return;

        boolean canAffectRock = beckonStage >= 2; //stages 2 and 3, not 1 (or 0)

        int convertedCount = 0;
        int maxPivotLvl = 0;

        int range = SRPConfigMobs.venkrolsiiiRange - 1;
        int rangeY = SRPConfigMobs.venkrolsiiiRangeY;
        if(SRPMixinsConfigHandler.deterrents.fixInfestedBlockLimit) {
            switch (beckonStage) {
                case 1:
                    range = SRPConfigMobs.venkrolRange - 1;
                    rangeY = SRPConfigMobs.venkrolRangeY;
                    break;
                case 2:
                    range = SRPConfigMobs.venkrolsiiRange - 1;
                    rangeY = SRPConfigMobs.venkrolsiiRangeY;
                    break;
            }
        }

        BlockPos blockPosAbove = startPos.up();
        Block blockAboveStartPos = worldIn.getBlockState(blockPosAbove).getBlock();

        AxisAlignedBB axisalignedbb = new AxisAlignedBB(startPos).grow(range+1, rangeY+1, range+1);
        List<EntityPStationaryArchitect> moblist = worldIn.getEntitiesWithinAABB(EntityPStationaryArchitect.class, axisalignedbb);
        if (!alwaysTrue) {
            if (moblist.isEmpty()) {
                srpmixins$trySpawnBeckon(worldIn, beckonStage, startPos, blockPosAbove, blockAboveStartPos);
                return;
            } else {
                boolean anyBeckonInDarkness = false;
                double distSqToNearestBeckon = range * range + 1;

                for (EntityPStationaryArchitect mob : moblist) {
                    if (mob.isPotionActive(SRPPotions.PIVOT_E))
                        maxPivotLvl = Math.max(mob.getActivePotionEffect(SRPPotions.PIVOT_E).getAmplifier() + 1, maxPivotLvl);

                    if (mob.getStageV() >= beckonStage && !anyBeckonInDarkness) {
                        BlockPos venPosUp = mob.getPosition().up();
                        if(SRPMixinsConfigHandler.deterrents.infestationAreaIsCircular) distSqToNearestBeckon = Math.min(distSqToNearestBeckon, startPos.distanceSq(mob.getPosition()));
                        if (
                            worldIn.getLightBrightness(venPosUp) < (float) SRPConfigSystems.rsBlockLight / 15.0F &&
                            worldIn.getLightFor(EnumSkyBlock.BLOCK, venPosUp) < SRPConfigSystems.rsBlockLight
                        )
                            anyBeckonInDarkness = true;
                    }
                }

                if (!anyBeckonInDarkness || SRPMixinsConfigHandler.deterrents.infestationAreaIsCircular && distSqToNearestBeckon > range * range) return;
            }
        }

        IBlockState stain = SRPBlocks.InfestedStain.getDefaultState().withProperty(BlockInfestedStain.STAGE, beckonStage);
        IBlockState rubble = SRPBlocks.InfestedRubble.getDefaultState().withProperty(BlockInfestedRubble.STAGE, beckonStage);

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (EnumFacing facing : EnumFacing.values()) {
            blockPos.setPos(startPos).move(facing);
            IBlockState state = worldIn.getBlockState(blockPos);

            Material material = state.getMaterial();
            if (material == Material.AIR) continue;

            Block block = state.getBlock();
            if (block instanceof IMetaName) continue;

            IBlockState newState = material == Material.ROCK ? rubble : stain;

            if (block instanceof BlockBase) {
                if (block == SRPBlocks.BiomeHeart || block == SRPBlocks.ColonyHeart) continue;
                if (material != Material.GROUND && material != Material.ROCK) continue;

                int storedStage = block.getMetaFromState(state);

                if (beckonStage > storedStage && (material != Material.ROCK || canAffectRock))
                    worldIn.setBlockState(blockPos, newState);
            } else if(!srpmixins$blockIsBlacklistedForInfestation(worldIn, blockPos, block, state)) {
                if (block.isWood(worldIn, blockPos)) {
                    worldIn.setBlockState(blockPos, SRPBlocks.InfestedTrunk.getDefaultState());
                    ++convertedCount;
                } else if (material == Material.GROUND || material == Material.GRASS || material == Material.SAND || (material == Material.ROCK && canAffectRock)) {
                    worldIn.setBlockState(blockPos, newState);
                    ++convertedCount;
                    spawnGenFeatureInfested(worldIn, blockPos.up(), rand);
                    spawnGenRoofInfested(worldIn, blockPos.down(), rand);
                }
            }
        }

        if (convertedCount != 0) {
            blockInfestedCount += convertedCount;
            if (SRPConfigSystems.useEvolution) {
                SRPSaveData data = SRPSaveDataInterface.get(worldIn, null, startPos);
                if (maxPivotLvl != 0) data.setTotalKills(worldIn.provider.getDimension(), SRPConfigSystems.valueBlock * convertedCount * SRPConfigSystems.pivotPointMultiplier * maxPivotLvl, true, worldIn, true);
                else data.setTotalKills(worldIn.provider.getDimension(), SRPConfigSystems.valueBlock * convertedCount, true, worldIn, true);
            }
        }
    }

    @Unique
    private static void infestationReversionLogic(World worldIn, BlockPos startPos, int beckonStage) {
        BlockPos blockPosAbove = startPos.up();
        Block blockAboveStartPos = worldIn.getBlockState(blockPosAbove).getBlock();

        IBlockState stain = SRPBlocks.InfestedStain.getDefaultState().withProperty(BlockInfestedStain.STAGE, beckonStage);
        IBlockState rubble = SRPBlocks.InfestedRubble.getDefaultState().withProperty(BlockInfestedRubble.STAGE, beckonStage);

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (EnumFacing facing : EnumFacing.values()) {
            blockPos.setPos(startPos).move(facing);
            IBlockState state = worldIn.getBlockState(blockPos);

            Material material = state.getMaterial();
            if (material == Material.AIR) continue;

            Block block = state.getBlock();
            if (block instanceof IMetaName) continue;

            if (block instanceof BlockBase && (material == Material.GROUND || material == Material.ROCK)) {
                if (block == SRPBlocks.BiomeHeart || block == SRPBlocks.ColonyHeart) continue;

                IBlockState newState = material == Material.ROCK ? rubble : stain;
                //int storedStage = block.getMetaFromState(state);

                //if (type == THISNEVERHAPPENS && storedStage <= 1) {
                worldIn.setBlockState(blockPos, newState);
                worldIn.updateBlockTick(blockPos, newState.getBlock(), 40, 5);
                //}
            }
        }

        Block blockAtStartPos = worldIn.getBlockState(startPos).getBlock();
        if (blockAtStartPos == SRPBlocks.InfestedStain) worldIn.setBlockState(startPos, SRPBlocks.optionalDirt.getDefaultState());
        else if (blockAtStartPos == SRPBlocks.InfestedRubble) worldIn.setBlockState(startPos, SRPBlocks.optionalRub.getDefaultState());

        if (blockAboveStartPos == Blocks.AIR || blockAboveStartPos == SRPBlocks.InfestedBush)
            worldIn.setBlockState(blockPosAbove, SRPBlocks.InfestRemain.getDefaultState());
    }

    @Unique
    private static void srpmixins$trySpawnBeckon(World worldIn, int beckonStage, BlockPos startPos, BlockPos blockPosAbove, Block blockAboveStartPos){
        if(beckonStage == 1) return;
        if(worldIn.rand.nextDouble() >= SRPConfigSystems.rsVenkrolEmpty) return;
        if((blockAboveStartPos != Blocks.AIR && !(blockAboveStartPos instanceof BlockBush))) return;
        if (worldIn.getLightBrightness(blockPosAbove) > 0.46666667F) return;
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, blockPosAbove) > 7) return;

        EntityVenkrol out = new EntityVenkrol(worldIn);
        out.setLocationAndAngles(startPos.getX(), startPos.getY() + 1, startPos.getZ(), 0.0F, 0.0F);
        worldIn.spawnEntity(out);
        out.cannotDespawn(true);
    }

    @Unique private static final Set<Block> srpmixins$listedBlocks = new HashSet<>();

    @Unique
    private static boolean srpmixins$blockIsBlacklistedForInfestation(World worldIn, BlockPos pos, Block block, IBlockState state) {
        float hardness = state.getBlockHardness(worldIn, pos);
        if(hardness < 0.0F || hardness > SRPConfigSystems.rsBlockIMaxH) return true;

        if (!state.isFullCube() || block.isPassable(worldIn, pos) ||
                block instanceof BlockBreakable ||
                block instanceof BlockContainer ||
                block instanceof BlockHorizontal ||
                block instanceof BlockTNT ||
                block instanceof IPlantable) return true;
        else {
            if(srpmixins$listedBlocks.contains(block)) return !SRPConfigSystems.blockBListWhite;
            ResourceLocation loc = block.getRegistryName();
            if(loc == null) return SRPConfigSystems.blockBListWhite;
            String blockName = loc.toString();
            boolean isInList = Arrays.stream(SRPConfigSystems.blockBList).anyMatch(blockName::contains);
            if(isInList) srpmixins$listedBlocks.add(block);

            return isInList != SRPConfigWorld.blockBBiomeListWhite;
        }
    }
}