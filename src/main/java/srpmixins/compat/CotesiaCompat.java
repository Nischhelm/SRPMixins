package srpmixins.compat;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrol;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.srpcotesia.biomes.BiomeSalt;
import com.srpcotesia.block.IUncorruptibleBlock;
import com.srpcotesia.config.ConfigMain;
import com.srpcotesia.util.ParasiteInteractions;
import com.srpcotesia.util.SRPCSaveData;
import com.srpcotesia.util.SaltUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class CotesiaCompat {
    public static boolean isUncorruptible(Block block) {
        return block instanceof IUncorruptibleBlock;
    }

    public static boolean spreadBiomeInject(World world, BlockPos pos, Random rand) {
        if (SRPCSaveData.isPostArmageddon(world) || world.getBiome(pos) instanceof BiomeSalt) {
            SaltUtil.spreadSaltSingle(world, pos, rand);
            return true;
        }
        return false;
    }

    public static void handleSpawnedBeckon(World world, boolean alwaysTrue, EntityVenkrol newBeckon) {
        ParasiteInteractions.setSalvageable(newBeckon, false);
        if (alwaysTrue && ConfigMain.rsBeckonOwn) {
            if (ParasiteInteractions.factorySpawnedVenkrol.get() == null) ParasiteInteractions.factorySpawnedVenkrol.set(false);

            if (ParasiteInteractions.factorySpawnedVenkrol.get()) {
                Entity possible = world.getEntityByID(ParasiteInteractions.factorySpawnedVenkrolID.get());
                if (possible instanceof EntityPStationaryArchitect) {
                    EntityPStationaryArchitect architect = (EntityPStationaryArchitect) possible;

                    EntityPlayer manager = ParasiteInteractions.getManager(architect);
                    ParasiteInteractions.setFactorySpawned(newBeckon, true);
                    if (manager != null) ParasiteInteractions.setManager(newBeckon, manager);
                }
            }
        }
    }

    public static void countInfestationPointsToPlayer(SRPSaveData instance, int id, int in, boolean plus, World world, boolean canChangePhase, boolean alwaysTrue) {
        Operation<Boolean> setTotalKillsOperation = params -> ((SRPSaveData) params[0]).setTotalKills((Integer) params[1], (Integer) params[2], (Boolean) params[3], (World) params[4], (Boolean) params[5]);

        if (alwaysTrue) {
            if (ParasiteInteractions.factorySpawnedVenkrol.get() == null) ParasiteInteractions.factorySpawnedVenkrol.set(false);

            if (ParasiteInteractions.factorySpawnedVenkrol.get()) {
                Entity possible = world.getEntityByID(ParasiteInteractions.factorySpawnedVenkrolID.get());
                if (possible instanceof EntityPStationaryArchitect) {
                    EntityPStationaryArchitect architect = (EntityPStationaryArchitect) possible;

                    EntityPlayer manager = ParasiteInteractions.getManager(architect);
                    if (manager != null) {
                        ParasiteInteractions.addKillsOnPlayerBehalf(instance, setTotalKillsOperation::call, manager, id, in, plus, world, canChangePhase, true);
                        return;
                    }
                }
            }
            ParasiteInteractions.factorySpawnedVenkrol.set(false);
        }
    }
}
