package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPWorldEntitySpawner.class)
public abstract class NexusCap {

    @WrapOperation(
            method = {"findChunksForSpawningOrigin","findChunksForSpawningVanilla"},
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntitySpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFFZ)Lnet/minecraftforge/fml/common/eventhandler/Event$Result;"),
            remap = false
    )
    private static Event.Result srpmixins_nexusCap(EntityLiving entity, World world, float x, float y, float z, boolean isSpawner, Operation<Event.Result> original) {
        if(entity instanceof EntityPStationaryArchitect) {
            int nexusCounter = (int) world.loadedEntityList.stream().filter(v -> v instanceof EntityPStationaryArchitect).count();
            if (nexusCounter > SRPMixinsConfigHandler.deterrents.nexusCap) return Event.Result.DENY;
        }

        return original.call(entity, world, x, y, z, isSpawner);
    }
}