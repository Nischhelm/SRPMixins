package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
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
public abstract class WaterParasiteCap {

    @WrapOperation(
            method = "findChunksForSpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntitySpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFFZ)Lnet/minecraftforge/fml/common/eventhandler/Event$Result;"),
            remap = false
    )
    private static Event.Result srpmixins_waterParasiteCap(EntityLiving entity, World world, float x, float y, float z, boolean isSpawner, Operation<Event.Result> original) {
        if(entity instanceof EntityInfSquid || entity instanceof EntityLum) {
            int waterCounter = (int) world.loadedEntityList.stream().filter(v -> v instanceof EntityInfSquid || v instanceof EntityLum).count();
            if (waterCounter > SRPMixinsConfigHandler.waterparas.waterParasiteCap) return Event.Result.DENY;
        }

        return original.call(entity, world, x, y, z, isSpawner);
    }
}