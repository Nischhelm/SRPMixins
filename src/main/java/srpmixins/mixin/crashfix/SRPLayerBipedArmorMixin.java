package srpmixins.mixin.crashfix;

import com.dhanantry.scapeandrunparasites.client.model.entity.SRPModelBiped;
import com.dhanantry.scapeandrunparasites.client.renderer.SRPLayerBipedArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPLayerBipedArmor.class)
public abstract class SRPLayerBipedArmorMixin extends LayerArmorBase<ModelBiped> {
	
	public SRPLayerBipedArmorMixin(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
	}
	
	@Inject(
			method = "renderArmorLayer",
			at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/client/renderer/SRPLayerBipedArmor;getModelFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/client/model/ModelBase;", ordinal = 0),
			cancellable = true,
			remap = false
	)
	private void srpmixins_srpSRPLayerBipedArmor_renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn, byte in, CallbackInfo ci) {
		if(!(this.getModelFromSlot(slotIn) instanceof SRPModelBiped)) {
			ci.cancel();
		}
	}
}