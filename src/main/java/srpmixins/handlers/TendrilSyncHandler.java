package srpmixins.handlers;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TendrilSyncHandler {
    public interface ISyncsTendrils{
        void srpmixins$onTracking();
    }

    @SubscribeEvent
    static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        if(event.getTarget() instanceof ISyncsTendrils)
            ((ISyncsTendrils) event.getTarget()).srpmixins$onTracking();
    }
}
