package srpmixins.capability.chunkphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.ChunkPhaseConfigProvider;

public class ChunkPhasesUtil {
    //With even spacing, we end up in the chunk south-east (++) of the chunk corner that would be the center of the region
    //With odd spacing, we end up in the chunk that is the center of the region
    public static ChunkPos blockPosToRegionChunkPos(BlockPos pos){
        int x = chunkPosToRegionChunkPos(pos.getX() >> 4);
        int z = chunkPosToRegionChunkPos(pos.getZ() >> 4);

        return new ChunkPos(x,z);
    }

    private static int chunkPosToRegionChunkPos(int chunkCoord){
        //Divide chunkCoord by spacing, floor towards -inf (north-west),
        // multiply again with spacing to stay in chunk coord units,
        // add half the spacing to get to center
        return Math.floorDiv(chunkCoord, ChunkPhaseConfigProvider.chunkPhasesSpacing) * ChunkPhaseConfigProvider.chunkPhasesSpacing + ChunkPhaseConfigProvider.chunkPhasesHalfSpacing;
    }

    //This is only called from region center chunks, to determine the biome at center of region
    //With even spacing, we are already at the chunk corner
    //With odd spacing, we should get the block that is +8 blocks away from the chunk 00 coord
    public static BlockPos getRegionChunkCenterBlock(ChunkPos pos) {
        int x = pos.x << 4; // * 16
        int z = pos.z << 4;
        if(ChunkPhaseConfigProvider.chunkPhasesSpacingIsOdd){
            x += 8;
            z += 8;
        }
        return new BlockPos(x, 63, z);
    }

    public static boolean chunkPosIsRegionCenter(ChunkPos pos){
        int regionX = chunkPosToRegionChunkPos(pos.x);
        int regionZ = chunkPosToRegionChunkPos(pos.z);
        return pos.x == regionX && pos.z == regionZ;
    }

    public static SRPSaveData getDataForPosition(BlockPos blockPos, World world, SRPSaveData defaultData){
        //Dimension has chunk phases enabled
        int dimension = world.provider.getDimension();
        boolean isInList = ChunkPhaseConfigProvider.chunkPhasesDimensionBlacklist.contains(dimension);
        if(isInList != SRPMixinsConfigHandler.chunkphases.dimensionBlacklistIsWhitelist) return defaultData;

        //Get region center chunk
        ChunkPos regionPos = ChunkPhasesUtil.blockPosToRegionChunkPos(blockPos);
        Chunk chunk = world.getChunkProvider().getLoadedChunk(regionPos.x, regionPos.z);
        if(chunk == null) return defaultData; //unloaded region centers just add their points to default SRPSaveData

        //Chunk capability is used as SRPSaveData
        ICapabilityEvoPoints chunkCap = chunk.getCapability(CapabilityEvoPointsHandler.CAP_EVOPOINTS, null);
        if(chunkCap != null)
            return (SRPSaveData) chunkCap;

        //Safety, shouldn't happen
        return defaultData;
    }
}
