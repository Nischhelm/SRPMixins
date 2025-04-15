package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(EntityPStationary.class)
public abstract class DeterrentLowPhaseDamage {
    @WrapOperation(
            method = "onLivingUpdate",
            at = @At(value="INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationary;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
    )
    private boolean srpmixins_onlyDamageWhitelistedDeterrents(EntityPStationary instance, DamageSource source, float amount, Operation<Boolean> original){
        int paraId = instance.getParasiteIDRegister();

        boolean listContainsThis = SRPMixinsConfigProvider.whiteListedDeterrents.contains(paraId);
        if(listContainsThis == SRPMixinsConfigHandler.deterrents.blackListDeterrents)
            return false; //don't attack

        return original.call(instance, source, amount); //this.attackEntityFrom(source, amount);
    }
}