package srpmixins.mixin.cothtweaks.camouflage;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.*;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHost;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHostII;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityMes;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.handlers.CamouflageHandler;

@Mixin({EntityPAdapted.class, EntityPAncient.class, EntityPFeral.class, EntityPHijacked.class, EntityPInfected.class, EntityPPreeminent.class, EntityPPrimitive.class, EntityPPure.class, EntityHost.class, EntityHostII.class, EntityMes.class, EntityNuuh.class})
public abstract class CamouflageOnAttacked {
    @ModifyExpressionValue(
            method = "attackEntityAsMob",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;epelChanceCOTH:D", remap = false)
    )
    private double srpmixins_fixCamouflage(double original, @Local EntityLivingBase target){
        return CamouflageHandler.getChanceFromState(original, target);
    }
}
