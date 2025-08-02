package srpmixins.compat.crafttweaker;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import srpmixins.SRPMixins;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.adaptation.ICapabilityAdaptation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(SRPMixins.MODID + ".StackHelper")
@SuppressWarnings("unused")
public class CT_ItemStackHelper {
    @ZenMethod
    private void removeAdaptation(IItemStack cstack){
        ItemStack stack = (ItemStack) cstack.getInternal();
        if(!(stack.getItem() instanceof ItemArmor)) return;
        ItemArmor.ArmorMaterial mat = ((ItemArmor)stack.getItem()).getArmorMaterial();
        if(mat != SRPItems.ARMOR_LIVING && mat != SRPItems.ARMOR_SENTIENT) return;

        ICapabilityAdaptation capAdaptation = stack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
        if(capAdaptation != null)
            capAdaptation.resetAdaptations();
        else if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.removeTag("sprresistances");
            nbt.removeTag("sprresistancei");
            nbt.removeTag("sprresistanceb");
        }
    }
}
