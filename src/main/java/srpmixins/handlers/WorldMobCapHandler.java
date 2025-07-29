package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import srpmixins.compat.CompatUtil;
import srpmixins.compat.SRPExtraCompat;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.event.WorldMobCapEvent;
import srpmixins.rules.MobCapRule;
import srpmixins.util.ParasiteCreatureType;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.HashMap;
import java.util.Map;

public class WorldMobCapHandler {

    @SubscribeEvent
    public static void onSetMobCap(WorldMobCapEvent event) {
        int dimId = event.getWorld().provider.getDimension();

        if (event.getType() != ParasiteCreatureType.PARASITE) {
            //For player phases and chunk phases the normal SRP checkspawn event handler blocks the spawns, this is just for performance
            if (!SRPMixinsConfigHandler.playerphases.enabled && !SRPMixinsConfigHandler.chunkphases.enabled)
                if (SRPSaveData.get(event.getWorld()).getEvolutionPhase(dimId) >= SRPConfigSystems.evolutionNoParasiteSpawnDenied)
                    event.setMobCap(0);
            return;
        }

        double multi = 0;
        if (SRPMixinsConfigHandler.playerphases.enabled) {
            //Avg over all players
            int counter = 0;
            for (EntityPlayer player : event.getWorld().playerEntities) {
                if (player.isSpectator()) continue;
                byte phase = SRPSaveDataInterface.get(event.getWorld(), player, null).getEvolutionPhase(dimId);
                multi += MobCapRule.getTotalMulti(dimId, phase);
                counter++;
            }
            if (counter != 0) multi /= counter;
            else multi = 1;
        } else if (SRPMixinsConfigHandler.chunkphases.enabled) {
            //Avg over all loaded chunks
            int counter = 0;
            for (ChunkPos pos : event.getEligibleChunks()) {
                byte phase = SRPSaveDataInterface.get(event.getWorld(), null, pos.getBlock(8, 0, 8)).getEvolutionPhase(dimId);
                multi += MobCapRule.getTotalMulti(dimId, phase);
                counter++;
            }
            if (counter != 0) multi /= counter;
            else multi = 1;
        } else {
            byte phase = SRPSaveData.get(event.getWorld()).getEvolutionPhase(dimId);
            multi = MobCapRule.getTotalMulti(dimId, phase);
        }

        //Set mob cap
        if (multi != 1) event.setMobCap((int) (event.getMobCap() * multi));
    }

    public static final Map<Integer, Integer> nexusCount = new HashMap<>();
    public static final Map<Integer, Integer> waterCount = new HashMap<>();
    public static final Map<Integer, Integer> gnatCount = new HashMap<>();
    public static final Map<Integer, Integer> scentCount = new HashMap<>();
    public static int end_simmermanCount = 0;

    @SubscribeEvent
    public static void onWorldServerTick(TickEvent.WorldTickEvent event){
        if(event.world.isRemote) return;
        if(event.phase != TickEvent.Phase.START) return;

        int dimId = event.world.provider.getDimension();

        if (SRPMixinsConfigHandler.deterrents.nexusCap >= 0)
            nexusCount.put(dimId, event.world.countEntities(EntityPStationaryArchitect.class));
        if (SRPMixinsConfigHandler.waterparas.waterParasiteCap >= 0)
            waterCount.put(dimId,
                    event.world.countEntities(EntityInfSquid.class) +
                    event.world.countEntities(EntityLum.class) +
                    (CompatUtil.srpextra.isLoaded() ? SRPExtraCompat.countWaterParasites(event.world) : 0)
            );
        gnatCount.put(dimId, event.world.countEntities(EntityAta.class));
        scentCount.put(dimId, event.world.countEntities(EntityParasiticScent.class));

        if(SRPMixinsConfigHandler.simmermen.endSimmermenCap > 0 && dimId == 1)
            end_simmermanCount = event.world.countEntities(EntityInfEnderman.class) + event.world.countEntities(EntityInfEndermanHead.class);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJoinWorld(EntityJoinWorldEvent event){
        //Keeping the cached mobCaps up to date during a tick
        World world = event.getWorld();
        if(world.isRemote) return;
        if(event.isCanceled()) return;
        if(!(event.getEntity() instanceof EntityParasiteBase)) return;
        EntityParasiteBase parasite = (EntityParasiteBase) event.getEntity();
        int dimId = event.getWorld().provider.getDimension();

        if (SRPMixinsConfigHandler.deterrents.nexusCap >= 0 && parasite instanceof EntityPStationaryArchitect)
            nexusCount.put(dimId, nexusCount.getOrDefault(dimId, 0));
        else if (SRPMixinsConfigHandler.waterparas.waterParasiteCap >= 0 && (parasite instanceof EntityInfSquid || parasite instanceof EntityLum || (CompatUtil.srpextra.isLoaded() && SRPExtraCompat.isWaterParasite(parasite))))
            waterCount.put(dimId, waterCount.getOrDefault(dimId, 0));
        else if(parasite instanceof EntityAta)
            gnatCount.put(dimId, gnatCount.getOrDefault(dimId, 0));
        else if(dimId == 1 && parasite instanceof EntityInfEnderman || parasite instanceof EntityInfEndermanHead)
            end_simmermanCount++;
    }
}
