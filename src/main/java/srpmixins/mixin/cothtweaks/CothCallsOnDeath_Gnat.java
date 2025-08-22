package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.handlers.CothNoDropsOnConversion;

@Mixin(EntityAta.class)
public abstract class CothCallsOnDeath_Gnat {
    @ModifyExpressionValue(
            method = "collideWithEntity",
            at = {
                    @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;convertEntityFeral(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/nbt/NBTTagCompound;Z[Ljava/lang/String;)Z"),
                    @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;hijackEntity(Lnet/minecraft/entity/EntityLivingBase;[Ljava/lang/String;)Z")
            },
            remap = false
    )
    private boolean srpmixins_callOnDeath(boolean succeeded, @Local EntityLivingBase entity){
        if(succeeded){
            entity.getCombatTracker().trackDamage(CothNoDropsOnConversion.COTH, entity.getHealth(), entity.getHealth());
            entity.onDeath(CothNoDropsOnConversion.COTH);
        }
        return succeeded;
    }
}
