package srpmixins.mixin.crashfix;

import com.dhanantry.scapeandrunparasites.client.model.entity.SRPModelBiped;
import com.dhanantry.scapeandrunparasites.client.renderer.SRPLayerBipedArmor;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPLayerBipedArmor.class)
public abstract class SRPLayerBipedArmorMixin {

	@ModifyExpressionValue(
			method = "renderArmorLayer",
			at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/client/renderer/SRPLayerBipedArmor;getModelFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/client/model/ModelBase;", ordinal = 0),
			remap = false
	)
	private ModelBase srpmixins_srpSRPLayerBipedArmor_renderArmorLayer(ModelBase original, @Cancellable CallbackInfo ci) {
		if(!(original instanceof SRPModelBiped)) ci.cancel();
		return original;
	}
}