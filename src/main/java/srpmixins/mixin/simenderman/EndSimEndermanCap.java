package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(ParasiteEventEntity.class)
public abstract class EndSimEndermanCap {
    @WrapOperation(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", ordinal = 0)
    )
    private static boolean simmermanCap(World world, Entity entity, Operation<Boolean> original){
        if(isSimmerman(entity))
            if(world.provider.getDimension() == 1) {
                int simmermancount = (int) world.loadedEntityList.stream().filter(EndSimEndermanCap::isSimmerman).count();
                if(simmermancount >= SRPMixinsConfigHandler.simmermen.endSimmermenCap) return false;
            }

        return original.call(world, entity); //world.spawnEntity(entity)
    }

    @Unique
    private static boolean isSimmerman(Entity entity){
        return entity instanceof EntityInfEnderman || entity instanceof EntityInfEndermanHead;
    }
}