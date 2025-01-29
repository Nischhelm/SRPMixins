package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
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
import srpmixins.handlers.SRPMixinsConfigHandler;

import javax.annotation.Nonnull;
import java.util.List;

@Mixin(WeaponToolRangeBase.class)
public class SentientBowEvolution extends ItemBow {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (SRPMixinsConfigHandler.weapons.disableSentientEvolution) return;
        if (!worldIn.isRemote && entityIn.ticksExisted % 80 == 0) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null && this == SRPItems.weapon_bow) {
                int srpkills = compound.getInteger("srpkills");

                if (srpkills > SRPConfig.weapon_livingSentient_HP_needed) {
                    compound.setInteger("srpkills", 0);
                    stack.shrink(1);
                    ItemStack newStack = new ItemStack(this.getNext(), 1);
                    if (SRPMixinsConfigHandler.weapons.fixSentientEvolutionNBT)
                        newStack.setTagCompound(stack.getTagCompound());
                    EntityItem entityitem = new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, newStack);
                    entityitem.setDefaultPickupDelay();
                    worldIn.spawnEntity(entityitem);
                    if (SRPConfig.thunderEnable)
                        worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, true));
                }
            }
        }
    }

    @Unique
    public Item getNext() {
        return this == SRPItems.weapon_bow ? SRPItems.weapon_bow_sentient : null;
    }

    @Inject(
            method = "func_77624_a",
            at = @At(value = "HEAD"),
            remap = false
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
