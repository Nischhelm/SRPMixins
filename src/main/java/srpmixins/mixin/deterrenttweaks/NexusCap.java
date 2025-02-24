package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPWorldEntitySpawner.class)
public abstract class NexusCap {

    @ModifyExpressionValue(
            method = "findChunksForSpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntitySpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFFZ)Lnet/minecraftforge/fml/common/eventhandler/Event$Result;"),
            remap = false
    )
    private static Event.Result nexusCap(Event.Result original, @Local EntityParasiteBase entity, @Local(argsOnly = true) WorldServer world) {
        if(entity instanceof EntityPStationaryArchitect) {
            int nexusCounter = (int) world.loadedEntityList.stream().filter(v -> v instanceof EntityPStationaryArchitect).count();
            if (nexusCounter > SRPMixinsConfigHandler.deterrents.nexusCap) return Event.Result.DENY;
        }

        return original;
    }
}