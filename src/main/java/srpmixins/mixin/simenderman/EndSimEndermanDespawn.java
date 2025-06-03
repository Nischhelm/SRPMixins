package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParasiteEventEntity.class)
public abstract class EndSimEndermanDespawn {

    @WrapOperation(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPInfected;cannotDespawn(Z)V"),
            remap = false
    )
    private static void srpmixins_endSimmermenDespawn(EntityPInfected parasite, boolean canDespawn, Operation<Void> original){
        if(parasite instanceof EntityInfEnderman || parasite instanceof EntityInfEndermanHead)
            if(parasite.world.provider.getDimension() == 1) {
                original.call(parasite, true);
                return;
            }
        original.call(parasite, canDespawn);
    }
}