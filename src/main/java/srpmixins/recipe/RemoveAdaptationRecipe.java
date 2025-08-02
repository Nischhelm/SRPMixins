package srpmixins.recipe;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.adaptation.ICapabilityAdaptation;
import srpmixins.config.SRPMixinsConfigHandler;

import javax.annotation.Nonnull;

public class RemoveAdaptationRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IShapedRecipe {
    private ItemStack outputStack = ItemStack.EMPTY;

    @Override
    public boolean matches(InventoryCrafting inv, @Nonnull World unused) {
        if(!(inv.getStackInSlot(4).getItem() instanceof ItemArmor)) return false;
        ItemArmor armor = (ItemArmor) inv.getStackInSlot(4).getItem();
        if(armor.getArmorMaterial() != SRPItems.ARMOR_LIVING && armor.getArmorMaterial() != SRPItems.ARMOR_SENTIENT) return false;
        for(int i : new int[]{0,2,6,8}) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack.isEmpty() && SRPMixinsConfigHandler.adaptation.adaptationResetItemTwo.isEmpty()) continue;
            else if(stack.isEmpty()) return false;
            ResourceLocation loc = stack.getItem().getRegistryName();
            if(loc == null) return false;
            if(!loc.toString().equals(SRPMixinsConfigHandler.adaptation.adaptationResetItemTwo)) return false;
        }
        for(int i : new int[]{1,3,5,7}) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack.isEmpty()) return false;
            ResourceLocation loc = stack.getItem().getRegistryName();
            if(loc == null) return false;
            if(!loc.toString().equals(SRPMixinsConfigHandler.adaptation.adaptationResetItem)) return false;
        }

        outputStack = inv.getStackInSlot(4).copy();
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        ICapabilityAdaptation capAdaptation = outputStack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
        if(capAdaptation != null)
            capAdaptation.resetAdaptations();
        else if(outputStack.hasTagCompound()) {
            NBTTagCompound nbt = outputStack.getTagCompound();
            nbt.removeTag("sprresistances");
            nbt.removeTag("sprresistancei");
            nbt.removeTag("sprresistanceb");
        }
        return outputStack;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        return this.getRecipeOutput().copy();
    }

    @Override
    public int getRecipeWidth() {
        return 3;
    }

    @Override
    public int getRecipeHeight() {
        return 3;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width == 3 && height == 3;
    }
}
