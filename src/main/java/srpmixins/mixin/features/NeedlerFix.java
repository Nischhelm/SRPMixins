
package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(SRPEffectBase.class)
public abstract class NeedlerFix {
    @Redirect(
            method = "effectNeedler",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"),
            remap = false
    )
    private float applyLimitCorrectly(float damage, float maxDamage){
        //Fix SRP using max(a,b) instead of correctly using min(a,b)
        return Math.min(damage,maxDamage);
    }

    @ModifyReceiver(
            method = "effectNeedler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;toString()Ljava/lang/String;")
    )
    private ResourceLocation stopCrashIfPlayer(ResourceLocation instance, @Local(argsOnly = true) EntityLivingBase entity){
        //Player entities are not in the forge entity list, so they need special handling
        if(entity instanceof EntityPlayer) return new ResourceLocation("srpmixins:disabled_auto_exception");
        return instance;
    }

    @WrapOperation(
            method = "effectNeedler",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;checkName(Ljava/lang/String;[Ljava/lang/String;Z)Z"),
            remap = false
    )
    private boolean dontCheckForPlayer(String mobId, String[] blackList, boolean isWhitelist, Operation<Boolean> original, @Local(argsOnly = true) EntityLivingBase entity){
        //If it's a player, use the custom config instead of SRP blacklist
        if(entity instanceof EntityPlayer) return !SRPMixinsConfigHandler.various.allowPlayerNeedler;
        return original.call(mobId, blackList, isWhitelist);
    }

    @Redirect(
            method = "effectNeedler",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;needlerDamage:F"),
            remap = false
    )
    private float varyMultiplierForPlayer(@Local(argsOnly = true) EntityLivingBase entity){
        //If it's a player, use the custom config instead of SRP health percentage (for first aid for example)
        if(entity instanceof EntityPlayer) return SRPMixinsConfigProvider.playerNeedlerMulti;
        return SRPConfigSystems.needlerDamage;
    }
}