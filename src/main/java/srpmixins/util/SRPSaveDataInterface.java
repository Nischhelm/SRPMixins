package srpmixins.util;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import srpmixins.capability.CapabilityEvoPointsHandler;
import srpmixins.capability.ICapabilityEvoPoints;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

import javax.annotation.Nullable;
import java.util.UUID;

public interface SRPSaveDataInterface {

    void setUUID(UUID uuid);

    //For player phases
    SRPSaveData getByPlayer(World world, UUID playerUUID);
    SRPSaveData getByBlock(World world, BlockPos blockPos);

    static SRPSaveData get(World world, @Nullable EntityPlayer player, @Nullable BlockPos blockPos){
        SRPSaveData data = SRPSaveData.get(world);
        if(SRPMixinsConfigHandler.phasepoints.playerPhases) {
            if(player != null) return ((SRPSaveDataInterface) data).getByPlayer(world, player.getUniqueID());
            else if(blockPos != null) return ((SRPSaveDataInterface) data).getByBlock(world, blockPos);
        }
        else if(SRPMixinsConfigHandler.phasepoints.chunkPhases) {
            int dimension = world.provider.getDimension();
            boolean isInList = SRPMixinsConfigProvider.chunkPhasesDimensionBlacklist.contains(dimension);
            if(isInList != SRPMixinsConfigHandler.phasepoints.chunkPhasesDimensionBlacklistIsWhitelist) return data;
            if(blockPos == null && player != null) blockPos = player.getPosition();
            if(blockPos == null) return data;
            Chunk chunk = world.getChunk(blockPos);
            ICapabilityEvoPoints chunkCap = chunk.getCapability(CapabilityEvoPointsHandler.CAP_EVOPOINTS, null);
            if(chunkCap != null) return (SRPSaveData) chunkCap;
        }
        return data;
    }
}