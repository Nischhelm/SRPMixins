package srpmultiplier.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParasiteDropChance {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void reduceParasitePartsDropChance(LivingDropsEvent event) {
        if (!SRPMultiplierConfigHandler.server.doMultipliers) return;
        for (EntityItem drop : event.getDrops()) {
            Item droppedItem = drop.getItem().getItem();
            if (!(droppedItem.getRegistryName().getNamespace().equals("srparasites"))) continue;
            if (droppedItem instanceof WeaponToolMeleeBase) continue;
            if (droppedItem instanceof WeaponToolArmorBase) continue;
            if (droppedItem instanceof WeaponToolRangeBase) continue;

            EntityLivingBase victim = event.getEntityLiving();
            if(!(victim instanceof EntityParasiteBase)) continue;

            int dim = victim.world.provider.getDimension();
            float dropChance = SRPMultiplierConfigHandler.dimensionDropMultipliers.get(dim);
            if (victim.getRNG().nextFloat() > dropChance)
                drop.setItem(ItemStack.EMPTY);
        }
    }
}