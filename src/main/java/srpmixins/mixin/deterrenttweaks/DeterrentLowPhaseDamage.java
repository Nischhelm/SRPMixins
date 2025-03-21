package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
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
        ResourceLocation loc = EntityList.getKey(instance);
        String typeOfThis = loc == null ? "" : loc.toString();

        boolean listContainsThis = SRPMixinsConfigProvider.whiteListedDeterrents.contains(typeOfThis);
        if(listContainsThis == SRPMixinsConfigHandler.deterrents.blackListDeterrents)
            return false; //don't attack

        return original.call(instance, source, amount); //this.attackEntityFrom(source, amount);
    }
}