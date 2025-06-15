package srpmixins.mixin.vanilla;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.adaptation.ICapabilityAdaptation;

@Mixin(Item.class)
public abstract class SyncAdaptation {
    /**
     * Writes and attaches the adaptation capability to an ItemStack's normal nbt tag to allow for syncing
     */
    @ModifyReturnValue(
            method = "getNBTShareTag",
            at = @At("RETURN"),
            remap = false
    )
    private NBTTagCompound srpmixins_sendAdaptation(NBTTagCompound original, ItemStack stack) {
        if(original != null && stack.getMaxStackSize() == 1 && stack.getItem() instanceof WeaponToolArmorBase) {
            ICapabilityAdaptation cap = stack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
            if(cap != null) {
                NBTBase capNBT = CapabilityAdaptationHandler.CAP_ADAPTATION.writeNBT(cap, null);
                if(capNBT != null) original.setTag("AdaptationCap", capNBT);
            }
        }
        return original;
    }

    /**
     * Reads the adaptation capability from an ItemStack's normal nbt tag to allow for syncing
     */
    @Inject(
            method = "readNBTShareTag",
            at = @At("HEAD"),
            remap = false
    )
    private void srpmixins_readAdaptation(ItemStack stack, NBTTagCompound nbt, CallbackInfo ci) {
        if(nbt != null && stack.getMaxStackSize() == 1 && stack.getItem() instanceof WeaponToolArmorBase) {
            NBTBase capNBT = nbt.getTag("AdaptationCap");
            if(capNBT instanceof NBTTagCompound) {
                ICapabilityAdaptation cap = stack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
                if(cap != null) {
                    CapabilityAdaptationHandler.CAP_ADAPTATION.readNBT(cap, null, capNBT);
                    //Tag is only needed for sync, remove it to avoid bloating the non-capability tag
                    nbt.removeTag("AdaptationCap");
                }
            }
        }
    }
}