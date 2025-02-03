package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import net.minecraft.entity.EntityList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

import java.util.Arrays;

@Mixin(EntityPStationary.class)
public abstract class DeterrentLowPhaseDamage {
    @Redirect(
            method = "onLivingUpdate",
            at = @At(value="INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationary;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
    )
    boolean onlyDamageWhitelistedDeterrents(EntityPStationary instance, DamageSource source, float amount){
        ResourceLocation resourcelocation = EntityList.getKey(instance);
        String typeOfThis = resourcelocation == null ? "" : resourcelocation.toString();

        boolean listContainsThis = Arrays.asList(SRPMixinsConfigHandler.deterrents.whiteListedDeterrents).contains(typeOfThis);
        if(listContainsThis == SRPMixinsConfigHandler.deterrents.blackListDeterrents)
            return false;
        return instance.attackEntityFrom(DamageSource.OUT_OF_WORLD, 1.0F);
    }
}