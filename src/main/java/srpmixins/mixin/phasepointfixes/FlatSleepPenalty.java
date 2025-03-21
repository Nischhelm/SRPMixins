package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPEventHandlerBus.class)
public abstract class FlatSleepPenalty {
    @Unique private static final Map<Integer,Long> srpmixins$lastWakeInDimension = new HashMap<>();

    @WrapWithCondition(
            method = "playerUp",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    private boolean srpmixins_flatSleepPenaltyMixin(SRPSaveData data, int id, int in, boolean plus, World world, boolean canChangePhase){
        if(!SRPMixinsConfigHandler.playerphases.enabled) {
            int dimension = world.provider.getDimension();
            long currTime = world.getTotalWorldTime();

            //Never saved a wakeup time for this dimension before
            if (!srpmixins$lastWakeInDimension.containsKey(dimension)) {
                srpmixins$lastWakeInDimension.put(dimension, currTime);
                return true;
            }

            //This player is not first to wake up, do not count
            if (currTime < srpmixins$lastWakeInDimension.get(dimension) + 1000) return false;
            //First player to wake up, save the time to stop others from adding points
            else srpmixins$lastWakeInDimension.put(dimension, currTime);
        }
        return true;
    }
}