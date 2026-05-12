package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import srpmixins.util.IIsTicking;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

public class TickSRPDataHandler {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        if(event.world.isRemote) return;
        if(event.world.getTotalWorldTime() % 20 != 17) return;

        //Tick SRPSaveData's of only the players that are online
        event.world.playerEntities.forEach(p -> {
            SRPSaveData data = SRPSaveDataInterface.get(event.world, p, null);
            ((IIsTicking)data).srpmixins$tick(20);
        });
    }
}
