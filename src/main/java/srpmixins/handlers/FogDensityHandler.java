package srpmixins.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FogDensityHandler {
    private static final Map<UUID, Float> densities = new HashMap<>();

    private FogDensityHandler() {
    }

    public static float getDensity(UUID player) {
        return densities.getOrDefault(player, 0.0F);
    }

    public static void setDensity(UUID player, float density) {
        densities.put(player, density);
    }

    public static void clearPlayer(UUID player) {
        densities.remove(player);
    }

    public static void clear() {
        densities.clear();
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.player.world.isRemote) clearPlayer(event.player.getUniqueID());
    }
}
