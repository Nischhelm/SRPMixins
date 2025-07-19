package srpmixins.mixin.spawning.entirefix.scentspawn;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.handlers.WorldMobCapHandler;

import java.util.Collections;
import java.util.List;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {

    @ModifyExpressionValue(
            method = "leaveScent",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;")
    )
    private static List<Entity> srpmixins_dontCount(List<Entity> original){
        return Collections.emptyList();
    }

    @ModifyVariable(
            method = "leaveScent",
            name = "count",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;scentCap:I")
    )
    private static int srpmixins_useOtherCounter(int original, @Local(argsOnly = true) World world){
        return WorldMobCapHandler.scentCount.getOrDefault(world.provider.getDimension(), 0);
    }
}
