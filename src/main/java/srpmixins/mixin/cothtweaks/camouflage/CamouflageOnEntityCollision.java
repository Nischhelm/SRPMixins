package srpmixins.mixin.cothtweaks.camouflage;

import com.dhanantry.scapeandrunparasites.block.BlockGore;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedRemain;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.handlers.CamouflageHandler;

@Mixin({BlockGore.class, BlockInfestedRemain.class})
public abstract class CamouflageOnEntityCollision{
    @ModifyExpressionValue(
            method = "onEntityCollision",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;epelChanceCOTH:D", remap = false)
    )
    private double srpmixins_fixCamouflage(double original, @Local EntityLivingBase target){
        return CamouflageHandler.getChanceFromState(original, target);
    }
}
