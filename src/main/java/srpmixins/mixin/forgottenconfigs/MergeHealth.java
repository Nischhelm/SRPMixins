package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParasiteEventEntity.class)
public abstract class MergeHealth {
    @Unique private static boolean srpmixins$isFromMerge = false;
    @Inject(
            method = "merge",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;spawnM(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;[Ljava/lang/String;IZLjava/lang/String;)V"),
            remap = false
    )
    private static void srpmixins_sendIsFromMerge(EntityParasiteBase entityin, int code, String name, CallbackInfoReturnable<Boolean> cir){
        srpmixins$isFromMerge = true;
    }

    @Inject(
            method = "spawnM",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private static void srpmixins_setHealth(EntityParasiteBase entityin, String[] out, int particle, boolean cannotDespawn, String name, CallbackInfo ci, @Local EntityLiving newEntity){
        if(srpmixins$isFromMerge)
            newEntity.setHealth((float) (newEntity.getMaxHealth()* SRPConfigSystems.mergeHealth));
    }

    @Inject(
            method = "spawnM",
            at = @At("RETURN"),
            remap = false
    )
    private static void srpmixins_resetIsFromMerge(EntityParasiteBase entityin, String[] out, int particle, boolean cannotDespawn, String name, CallbackInfo ci){
        srpmixins$isFromMerge = false;
    }
}