package srpmixins.mixin.potions;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPEffectBase.class)
public abstract class FosterNerf {
    @WrapWithCondition(
            method = "effectFoster",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPMalleable;increaseAllResistances()V")
    )
    private boolean srpmixins_nerfFoster(EntityPMalleable instance){
        return SRPMixinsConfigHandler.potions.fosterChance < 0 || instance.getRNG().nextFloat() < SRPMixinsConfigHandler.potions.fosterChance;
    }
}
