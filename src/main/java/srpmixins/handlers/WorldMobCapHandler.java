package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
        } else if (SRPMixinsConfigHandler.chunkphases.enabled) {
            //Avg over all loaded chunks
            int counter = 0;
            for (ChunkPos pos : event.getEligibleChunks()) {
                byte phase = SRPSaveDataInterface.get(event.getWorld(), null, pos.getBlock(8, 0, 8)).getEvolutionPhase(dimId);
                multi += MobCapRule.getTotalMulti(dimId, phase);
                counter++;
            }
            if (counter != 0) multi /= counter;
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

    @SubscribeEvent
    public static void onWorldServerTick(TickEvent.WorldTickEvent event){
        if(event.world.isRemote) return;
        if(event.phase != TickEvent.Phase.START) return;

        if (SRPMixinsConfigHandler.deterrents.nexusCap >= 0)
            nexusCount.put(event.world.provider.getDimension(), event.world.countEntities(EntityPStationaryArchitect.class));
        if (SRPMixinsConfigHandler.waterparas.waterParasiteCap >= 0)
            waterCount.put(event.world.provider.getDimension(), event.world.countEntities(EntityInfSquid.class) + event.world.countEntities(EntityLum.class));
        gnatCount.put(event.world.provider.getDimension(), event.world.countEntities(EntityAta.class));
        scentCount.put(event.world.provider.getDimension(), event.world.countEntities(EntityParasiticScent.class));
    }
}
