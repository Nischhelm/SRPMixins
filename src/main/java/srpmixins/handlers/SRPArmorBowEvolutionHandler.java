package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;

public class SRPArmorBowEvolutionHandler {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(SRPMixinsConfigHandler.weapons.disableSentientEvolution) return;
        EntityLivingBase victim = event.getEntityLiving();
        if(victim == null || victim.world.isRemote) return;
        DamageSource source = event.getSource();
        if(source == null) return;
        if(!(source.getTrueSource() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) source.getTrueSource();

        int victimMaxHealth = (int) victim.getMaxHealth();

        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
            ItemStack stack = player.getItemStackFromSlot(slot);
            if(stack.isEmpty()) continue;
            if(stack.getItem() instanceof WeaponToolRangeBase || stack.getItem() instanceof WeaponToolArmorBase){
                NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                compound.setInteger("srpkills", compound.getInteger("srpkills") + victimMaxHealth);
                stack.setTagCompound(compound);
            }
        }
    }
}
