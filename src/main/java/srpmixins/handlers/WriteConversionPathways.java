package srpmixins.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import srpmixins.config.SRPMixinsConfigProvider;

public class WriteConversionPathways {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if(event.player.world.isRemote) return;
        SRPMixinsConfigProvider.writeConversionLockConfig();
    }
}