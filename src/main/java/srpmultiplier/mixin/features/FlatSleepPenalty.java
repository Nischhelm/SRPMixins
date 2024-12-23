package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.HashMap;

@Mixin(SRPEventHandlerBus.class)
public abstract class FlatSleepPenalty {
    @Unique
    HashMap<Integer,Long> lastWake = new HashMap<>();

    @Redirect(
            method = "playerUp",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    boolean flatSleepPenaltyMixin(SRPSaveData data, int id, int in, boolean plus, World world, boolean canChangePhase){
        if(SRPMultiplierConfigHandler.server.flatSleepPenalty && !SRPMultiplierConfigHandler.server.playerPhases) {
            int dimension = world.provider.getDimension();
            long currentWT = world.getWorldTime();
            if (!lastWake.containsKey(dimension)) {
                lastWake.put(dimension, currentWT);
                return data.setTotalKills(id,in, true, world, true);
            }

            if (currentWT < lastWake.get(dimension) + 1000) {
                return false;
            } else {
                lastWake.put(dimension, currentWT);
            }
        }
        return data.setTotalKills(id,in, true, world, true);
    }
}