package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.adaptation.ICapabilityAdaptation;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.List;

@Mixin(WeaponToolArmorBase.class)
public class SentientArmorEvolution extends ItemArmor {
    public SentientArmorEvolution(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
    }

    @Inject(
            method = "onUpdate",
            at = @At("TAIL")
    )
    public void srpmixins_countKillsOnArmor(ItemStack oldStack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci) {
        if (SRPMixinsConfigHandler.weapons.disableSentientEvolution) return;
        if (!worldIn.isRemote && entityIn.ticksExisted % 80 == 0) {
            NBTTagCompound compound = oldStack.getTagCompound();
            if (compound != null && this.getArmorMaterial() == SRPItems.ARMOR_LIVING) {
                int srpkills = compound.getInteger("srpkills");

                if (srpkills > SRPConfig.weapon_livingSentient_HP_needed) {
                    compound.setInteger("srpkills", 0);
                    ItemStack newStack = new ItemStack(this.srpmixins$getNext(), 1);
                    if (SRPMixinsConfigHandler.weapons.fixSentientEvolutionNBT) {
                        newStack.setTagCompound(oldStack.getTagCompound().copy());
                        if(SRPMixinsConfigHandler.adaptation.overhaulAdaptation){
                            ICapabilityAdaptation adaCap = oldStack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
                            ICapabilityAdaptation adaCapNew = newStack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
                            if(adaCap != null && adaCapNew != null) adaCapNew.copyAdaptationsFrom(adaCap);
                        }
                    }
                    if(!SRPMixinsConfigHandler.weapons.keepEvolvedGear || oldStack.getCount() > 1 || !(entityIn instanceof EntityPlayer)) { //default behavior
                        oldStack.shrink(1);
                        EntityItem entityitem = new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, newStack);
                        entityitem.setDefaultPickupDelay();
                        worldIn.spawnEntity(entityitem);
                    } else {
                        EntityEquipmentSlot thisSlot = null;
                        switch (itemSlot) {
                            case 3: thisSlot = EntityEquipmentSlot.HEAD; break;
                            case 2: thisSlot = EntityEquipmentSlot.CHEST; break;
                            case 1: thisSlot = EntityEquipmentSlot.LEGS; break;
                            case 0: thisSlot = EntityEquipmentSlot.FEET; break;
                        }
                        if(thisSlot != null && ((EntityPlayer) entityIn).getItemStackFromSlot(thisSlot) == oldStack) {
                            oldStack.shrink(1);
                            entityIn.setItemStackToSlot(thisSlot, newStack);
                        }
                    }
                    if (SRPConfig.thunderEnable)
                        worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, true));
                }
            }
        }
    }

    @Unique
    public Item srpmixins$getNext() {
        if (this.getArmorMaterial() != SRPItems.ARMOR_LIVING) return null;
        switch (this.armorType) {
            case HEAD: return SRPItems.armor_helmetSentient;
            case CHEST: return SRPItems.armor_chestSentient;
            case LEGS: return SRPItems.armor_pantsSentient;
            case FEET: return SRPItems.armor_bootsSentient;
            default: return null;
        }
    }

    @Inject(
            method = "addInformation",
            at = @At(value = "HEAD")
    )
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn, CallbackInfo ci) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (SRPMixinsConfigHandler.weapons.disableSentientEvolution) return;
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null && (this.getArmorMaterial() == SRPItems.ARMOR_LIVING || !SRPMixinsConfigHandler.weapons.removeSentientSRPKillsTooltip)) {
            tooltip.add(TextFormatting.BLUE + "---> " + compound.getInteger("srpkills"));
            tooltip.add(TextFormatting.BLUE + "  ");
        }
    }
}