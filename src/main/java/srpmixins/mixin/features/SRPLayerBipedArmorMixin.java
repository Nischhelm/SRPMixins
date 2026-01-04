package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.client.model.entity.SRPModelBiped;
import com.dhanantry.scapeandrunparasites.client.renderer.SRPLayerBipedArmor;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPLayerBipedArmor.class)
public abstract class SRPLayerBipedArmorMixin extends LayerArmorBase<ModelBiped> {
	public SRPLayerBipedArmorMixin(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
	}

	@ModifyExpressionValue(
			method = "renderArmorLayer",
			at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/client/renderer/SRPLayerBipedArmor;getModelFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/client/model/ModelBase;", ordinal = 0),
			remap = false
	)
	private ModelBase srpmixins_srpSRPLayerBipedArmor_renderArmorLayer(
            ModelBase original,
			@Cancellable CallbackInfo ci,
			@Local(name = "entityLivingBaseIn") EntityLivingBase entity,
			@Local(name = "slotIn") EntityEquipmentSlot slot,
			@Local(name = "itemstack") ItemStack stack,
			@Share("afterHook") LocalRef<ModelBiped> modelAfterHook
	) {
		if(!(original instanceof SRPModelBiped)) ci.cancel();
		ModelBiped model = this.getArmorModelHook(entity, stack, slot, (ModelBiped) original);
		modelAfterHook.set(model);
		if(!(model instanceof SRPModelBiped)) ci.cancel();
		return original;
	}

	@Redirect(
			method = "renderArmorLayer",
			at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/client/renderer/SRPLayerBipedArmor;getArmorModelHook(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;Lnet/minecraft/client/model/ModelBiped;)Lnet/minecraft/client/model/ModelBiped;"),
			remap = false
	)
	private ModelBiped srpmixins_dontCallEventThreeTimes(
			SRPLayerBipedArmor instance, EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model,
			@Share("afterHook") LocalRef<ModelBiped> modelAfterHook
	){
		return modelAfterHook.get();
	}
}