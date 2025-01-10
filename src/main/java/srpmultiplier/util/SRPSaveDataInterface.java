package srpmultiplier.util;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.UUID;

public interface SRPSaveDataInterface {

    void setUUID(UUID uuid);

    SRPSaveData getByPlayer(World world, UUID playerUUID);

    SRPSaveData getByBlock(World world, BlockPos blockPos);

    static SRPSaveData get(World world, EntityPlayer player, BlockPos blockPos){
        SRPSaveData data = SRPSaveData.get(world);
        if(SRPMultiplierConfigHandler.phasepoints.playerPhases) {
            if(player != null)
                return ((SRPSaveDataInterface) data).getByPlayer(world, player.getUniqueID());
            else if(blockPos != null)
                return ((SRPSaveDataInterface) data).getByBlock(world, blockPos);
        }
        return data;
    }
}