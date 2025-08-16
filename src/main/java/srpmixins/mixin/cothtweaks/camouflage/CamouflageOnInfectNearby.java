package srpmixins.mixin.cothtweaks.camouflage;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.handlers.CamouflageHandler;

@Mixin({EntityPInfected.class, SRPEffectBase.class})
public abstract class CamouflageOnInfectNearby{
    @ModifyExpressionValue(
            method = "InfectNearby",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;epelChanceCOTH:D"),
            remap = false
    )
    private double srpmixins_fixCamouflage(double original, @Local(name = "mob") EntityLivingBase target){
        return CamouflageHandler.getChanceFromState(original, target);
    }
}
