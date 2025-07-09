package srpmixins.mixin.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.ParasiteCreatureType;

@Debug(export=true)
@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract World init();

    @WrapOperation(
            method = "countEntities(Lnet/minecraft/entity/EnumCreatureType;Z)I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isCreatureType(Lnet/minecraft/entity/EnumCreatureType;Z)Z"),
            remap = false
    )
    private boolean srpmixins_parasitesDontCountToHostileCap(Entity instance, EnumCreatureType type, boolean forSpawnCount, Operation<Boolean> original){
        boolean originalValue = original.call(instance, type, forSpawnCount);
        if(type != EnumCreatureType.MONSTER) return originalValue;
        return originalValue && ParasiteCreatureType.PARASITE.getCreatureClass().isAssignableFrom(instance.getClass());
    }
}
