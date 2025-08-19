package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.handlers.CothNoDropsOnConversion;

@Mixin(SRPEffectBase.class)
public abstract class CothCallsOnDeath {
    @Inject(
            method = "effectCOTH",
            at = {
                    @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;convertEntity(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/nbt/NBTTagCompound;Z[Ljava/lang/String;)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnInsider(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V", shift = At.Shift.AFTER)
            },
            remap = false
    )
    private void srpmixins_callOnDeath(EntityLivingBase entity, int amplifier, CallbackInfo ci){
        if(entity.isDead) entity.onDeath(CothNoDropsOnConversion.COTH);
    }
}
