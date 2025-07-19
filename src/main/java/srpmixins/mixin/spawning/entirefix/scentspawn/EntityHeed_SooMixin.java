package srpmixins.mixin.spawning.entirefix.scentspawn;

import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHeed;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntitySoo;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.handlers.WorldMobCapHandler;

import java.util.Collections;
import java.util.List;

@Mixin(value = {EntityHeed.class, EntitySoo.class})
public abstract class EntityHeed_SooMixin extends Entity {
    public EntityHeed_SooMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;")
    )
    private List<Entity> srpmixins_dontCount(List<Entity> original){
        return Collections.emptyList();
    }

    @ModifyVariable(
            method = "onLivingUpdate",
            name = "count",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;scentCap:I", remap = false)
    )
    private int srpmixins_useOtherCounter(int original){
        return WorldMobCapHandler.scentCount.getOrDefault(this.world.provider.getDimension(), 0);
    }
}
