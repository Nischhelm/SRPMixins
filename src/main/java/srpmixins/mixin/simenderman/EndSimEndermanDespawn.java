package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ParasiteEventEntity.class)
public abstract class EndSimEndermanDespawn {

    @ModifyArg(
            method = {"convertEntity", "convertEntityFeral"},
            at = @At(value="INVOKE",target = "cannotDespawn(Z)V"), //intellij complains for no reason
            remap = false
    )
    private static boolean srpmixins_endSimmermenDespawn(boolean originalCanDespawn, @Local(name = "outOne") Entity parasite){
        if(parasite instanceof EntityInfEnderman || parasite instanceof EntityInfEndermanHead || parasite instanceof EntityFerEnderman)
            if(parasite.world.provider.getDimension()==1)
                return true;
        return originalCanDespawn;
    }
}