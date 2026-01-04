package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
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
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.List;

@Mixin(WeaponToolRangeBase.class)
public class SentientBowEvolution extends ItemBow {

    @Inject(
            method = "onUpdate",
            at = @At("TAIL")
    )
    public void srpmixins_countKillsOnBow(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci) {
        if (SRPMixinsConfigHandler.weapons.disableSentientEvolution) return;
        if (!worldIn.isRemote && entityIn.ticksExisted % 80 == 0) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null && this == SRPItems.weapon_bow) {
                int srpkills = compound.getInteger("srpkills");

                if (srpkills > SRPConfig.weapon_livingSentient_HP_needed) {
                    compound.setInteger("srpkills", 0);
                    ItemStack newStack = new ItemStack(this.srpmixins$getNext(), 1);
                    if (SRPMixinsConfigHandler.weapons.fixSentientEvolutionNBT)
                        newStack.setTagCompound(stack.getTagCompound().copy());

                    if(!SRPMixinsConfigHandler.weapons.keepEvolvedGear || stack.getCount() > 1) { //default behavior
                        stack.shrink(1);
                        EntityItem entityitem = new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, newStack);
                        entityitem.setDefaultPickupDelay();
                        worldIn.spawnEntity(entityitem);
                    } else {
                        EntityEquipmentSlot thisSlot = null;
                        if(entityIn instanceof EntityPlayer){ //This should always be the case except if some mod modifies it
                            EntityPlayer player = (EntityPlayer) entityIn;
                            boolean isMainhand = player.getHeldItemMainhand() == stack;
                            boolean isOffhand = player.getHeldItemOffhand() == stack;
                            if(isMainhand) thisSlot = EntityEquipmentSlot.MAINHAND;
                            else if(isOffhand) thisSlot = EntityEquipmentSlot.OFFHAND;
                        }
                        if(thisSlot != null) {
                            stack.shrink(1);
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
        return this == SRPItems.weapon_bow ? SRPItems.weapon_bow_sentient : null;
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
        if (compound != null && (this == SRPItems.weapon_bow || !SRPMixinsConfigHandler.weapons.removeSentientSRPKillsTooltip)) {
            tooltip.add(TextFormatting.BLUE + "---> " + compound.getInteger("srpkills"));
            tooltip.add(TextFormatting.BLUE + "  ");
        }
    }
}
