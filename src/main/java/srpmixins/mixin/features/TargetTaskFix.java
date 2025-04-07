package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPEventHandlerBus.class)
public abstract class TargetTaskFix {
/*    @Shadow(remap = false) protected abstract void setNewCreatureTask(EntityCreature entity, String mobName);

    @ModifyExpressionValue(
            method = "onEntitySpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/EntityJoinWorldEvent;getEntity()Lnet/minecraft/entity/Entity;", ordinal = 11),
             remap = false
    )
    private Entity srpmixins_targetTaskForAllLivingBase(Entity original, @Local(argsOnly = true) EntityJoinWorldEvent event, @Local String mobName){
        //SRP only checks EntityCreature instead of all EntityLivingBase
        if(original instanceof EntityLivingBase && !(original instanceof EntityCreature))
            this.setNewCreatureTask((EntityCreature) event.getEntity(), mobName);
        return original;
    }*/
}
