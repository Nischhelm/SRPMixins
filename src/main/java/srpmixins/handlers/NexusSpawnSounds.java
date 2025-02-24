package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.*;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NexusSpawnSounds {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void soundsOnSpecialSpawns(LivingSpawnEvent.SpecialSpawn event) {
        if (event.isCanceled() || !SRPConfigSystems.rsSounds) return;
        Entity e = event.getEntity();
        if (!(e instanceof EntityPStationaryArchitect)) return;

        if (e instanceof EntityVenkrolSIV){ event.getEntity().playSound(SRPSounds.VENKROLSIV, 100.0F, 1.0F); return; }
        if (e instanceof EntityVenkrolSIII){ event.getEntity().playSound(SRPSounds.VENKROLSIII, 100.0F, 1.0F); return; }
        if (e instanceof EntityVenkrolSII){ event.getEntity().playSound(SRPSounds.VENKROLSII, 100.0F, 1.0F); return; }

        if (e instanceof EntityDodSIV){ event.getEntity().playSound(SRPSounds.DODSIV, 100.0F, 1.0F); return; }
        if (e instanceof EntityDodSIII){ event.getEntity().playSound(SRPSounds.DODSIII, 100.0F, 1.0F); return; }
        if (e instanceof EntityDodSII){ event.getEntity().playSound(SRPSounds.DODSII, 100.0F, 1.0F); return; }
    }

}