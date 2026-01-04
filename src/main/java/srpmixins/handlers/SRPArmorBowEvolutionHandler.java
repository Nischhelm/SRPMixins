package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.folders.WeaponConfig;

public class SRPArmorBowEvolutionHandler {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        EntityLivingBase victim = event.getEntityLiving();
        if(victim == null || victim.world.isRemote) return;
        if(SRPMixinsConfigHandler.weapons.onlyParasites && !(victim instanceof EntityParasiteBase)) return;
        DamageSource source = event.getSource();
        if(source == null) return;
        if(!(source.getTrueSource() instanceof EntityPlayer)) return;

        addPointsToEquipment((EntityPlayer) source.getTrueSource(), (int) victim.getMaxHealth(), SRPMixinsConfigHandler.weapons.armorEvoType == WeaponConfig.EnumArmorEvolution.DEAL_DAMAGE, true);

        ItemStack offhandStack = ((EntityPlayer) source.getTrueSource()).getHeldItemOffhand();
        if(offhandStack.getItem() instanceof WeaponToolMeleeBase){ //by default, only mainhand weapons get points
            NBTTagCompound compound = offhandStack.hasTagCompound() ? offhandStack.getTagCompound() : new NBTTagCompound();
            compound.setInteger("srpkills", compound.getInteger("srpkills") + (int) victim.getMaxHealth());
            offhandStack.setTagCompound(compound);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event){
        if(SRPMixinsConfigHandler.weapons.armorEvoType != WeaponConfig.EnumArmorEvolution.TAKE_DAMAGE) return;
        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
        if(event.getSource().isUnblockable()) return;
        if(SRPMixinsConfigHandler.weapons.onlyParasites && !(event.getSource().getTrueSource() instanceof EntityParasiteBase)) return;

        addPointsToEquipment((EntityPlayer) event.getEntityLiving(), (int) event.getAmount(), true, false);
    }

    private static void addPointsToEquipment(EntityPlayer player, int points, boolean allowArmor, boolean allowBow){
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
            ItemStack stack = player.getItemStackFromSlot(slot);
            if(stack.isEmpty()) continue;
            if(allowBow && stack.getItem() instanceof WeaponToolRangeBase || allowArmor && stack.getItem() instanceof WeaponToolArmorBase && slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR){
                NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                compound.setInteger("srpkills", compound.getInteger("srpkills") + points);
                stack.setTagCompound(compound);
            }
        }
    }
}
