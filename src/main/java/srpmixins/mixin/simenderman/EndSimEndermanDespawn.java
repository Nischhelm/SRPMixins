package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ParasiteEventEntity.class)
public abstract class EndSimEndermanDespawn {

    @ModifyArg(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPInfected;cannotDespawn(Z)V"),
            remap = false
    )
    private static boolean endSimmermenDespawnMixin(boolean originalCanDespawn, @Local EntityPInfected parasite){
        if(parasite instanceof EntityInfEnderman || parasite instanceof EntityInfEndermanHead)
            if(parasite.world.provider.getDimension()==1)
                return true;
        return originalCanDespawn;
    }
}