package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrol;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParasiteEventWorld.class)
public abstract class StopInfestedBeckonDespawn {
    @WrapWithCondition(
            method = "canInfestBlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/monster/deterrent/nexus/EntityVenkrol;cannotDespawn(Z)V"),
            remap = false
    )
    private static boolean srpmixins_stopInfestedBeckonDespawn(EntityVenkrol instance, boolean b){
        return false; //beckons spawned by infested blocks can (also) not despawn
    }
}
