package srpmixins.util;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import srpmixins.capability.CapabilityEvoPointsHandler;
import srpmixins.capability.ICapabilityEvoPoints;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

public class ChunkPhasesUtil {
    //With even spacing, we end up in the chunk south-east (++) of the chunk corner that would be the center of the region
    //With odd spacing, we end up in the chunk that is the center of the region
    public static ChunkPos getRegionChunkFromBlock(BlockPos pos){
        //Divide blockPos by 16 x spacing, truncate
        int x = (pos.getX() >> 4) / SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing;
        int z = (pos.getZ() >> 4) / SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing;

        int halfSpacing = SRPMixinsConfigProvider.chunkPhasesHalfSpacing;
        x = x * SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing + halfSpacing;
        z = z * SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing + halfSpacing;

        return new ChunkPos(x,z);
    }

    //With even spacing, we are already at the chunk corner
    //With odd spacing, we should get the block that is +8 blocks away from the chunk 00 coord
    public static BlockPos getCenterBlockFromChunkPos(ChunkPos pos) {
        int x = pos.x << 4; // x 16
        int z = pos.z << 4;
        boolean isOdd = (SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing & 1) == 1;
        if(isOdd){
            x += 8;
            z += 8;
        }
        return new BlockPos(x, 63, z);
    }

    public static boolean chunkIsRegionCenter(ChunkPos pos){
        int regionX = (pos.x / SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing) + SRPMixinsConfigProvider.chunkPhasesHalfSpacing;
        int regionZ = (pos.z / SRPMixinsConfigHandler.chunkphases.chunkPhasesSpacing) + SRPMixinsConfigProvider.chunkPhasesHalfSpacing;
        return pos.x == regionX && pos.z == regionZ;
    }

    public static SRPSaveData getForPosition(BlockPos blockPos, World world, SRPSaveData defaultData){
        //Dimension has chunk phases enabled
        int dimension = world.provider.getDimension();
        boolean isInList = SRPMixinsConfigProvider.chunkPhasesDimensionBlacklist.contains(dimension);
        if(isInList != SRPMixinsConfigHandler.chunkphases.chunkPhasesDimensionBlacklistIsWhitelist) return defaultData;

        //Get region center chunk
        ChunkPos regionPos = ChunkPhasesUtil.getRegionChunkFromBlock(blockPos);
        Chunk chunk = world.getChunk(regionPos.x, regionPos.z);

        //Chunk capability is used as SRPSaveData
        ICapabilityEvoPoints chunkCap = chunk.getCapability(CapabilityEvoPointsHandler.CAP_EVOPOINTS, null);
        if(chunkCap != null) return (SRPSaveData) chunkCap;

        return defaultData;
    }
}
