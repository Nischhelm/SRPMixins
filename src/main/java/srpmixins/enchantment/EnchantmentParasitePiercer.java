package srpmixins.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextFormatting;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import javax.annotation.Nonnull;

public class EnchantmentParasitePiercer extends Enchantment {

    public EnchantmentParasitePiercer(String name, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot... slots) {
        super(rarity, type, slots);
        this.name = name;
        this.setRegistryName(SRPMixins.MODID, name);
    }

    @Override
    public int getMaxLevel() {
        return SRPMixinsConfigHandler.enchantments.piercer.maxLvl;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + 10 * (enchantmentLevel-1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return SRPMixinsConfigHandler.enchantments.piercer.isTreasure;
    }

    @Override
    @Nonnull
    public String getTranslatedName(int level) {
        return this.getPrefix() + super.getTranslatedName(level);
    }

    @Override
    public boolean canApplyTogether(@Nonnull Enchantment ench) {
        return (!SRPMixinsConfigHandler.enchantments.piercer.incompatible || !ench.equals(Enchantments.FIRE_ASPECT)) && super.canApplyTogether(ench);
    }

    public String getPrefix() {
        return TextFormatting.DARK_PURPLE+"";
    }
}