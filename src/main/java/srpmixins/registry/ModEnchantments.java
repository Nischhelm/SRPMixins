package srpmixins.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.enchantment.EnchantmentParasitePiercer;
import srpmixins.enchantment.EnchantmentParasiteSlicer;

@Mod.EventBusSubscriber
public class ModEnchantments {
    public static EnchantmentParasitePiercer parasitePiercer = new EnchantmentParasitePiercer("parasite_piercer", Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.MAINHAND);
    public static EnchantmentParasiteSlicer parasiteSlicer = new EnchantmentParasiteSlicer("parasite_slicer", Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.MAINHAND);

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        if(SRPMixinsConfigHandler.enchantments.piercer.enabled) event.getRegistry().register(parasitePiercer);
        if(SRPMixinsConfigHandler.enchantments.slicer.enabled) event.getRegistry().register(parasiteSlicer);
    }
}
