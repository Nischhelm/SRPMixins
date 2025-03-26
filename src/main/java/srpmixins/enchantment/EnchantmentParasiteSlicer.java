package srpmixins.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextFormatting;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import javax.annotation.Nonnull;

public class EnchantmentParasiteSlicer extends Enchantment {

    public EnchantmentParasiteSlicer(String name, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot... slots) {
        super(rarity, type, slots);
        this.name = name;
        this.setRegistryName(SRPMixins.MODID, name);
    }

    @Override
    public int getMaxLevel() {
        return SRPMixinsConfigHandler.enchantments.slicer.maxLvl;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + 10 * (enchantmentLevel-1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return SRPMixinsConfigHandler.enchantments.slicer.isTreasure;
    }

    @Override
    public float calcDamageByCreature(int level, @Nonnull EnumCreatureAttribute creatureType) {
        return SRPMixinsConfigHandler.enchantments.slicer.dmg * level;
    }

    @Override
    @Nonnull
    public String getTranslatedName(int level) {
        return this.getPrefix() + super.getTranslatedName(level);
    }

    @Override
    public boolean canApplyTogether(@Nonnull Enchantment ench) {
        return (!SRPMixinsConfigHandler.enchantments.slicer.incompatible || !(ench instanceof EnchantmentDamage)) && super.canApplyTogether(ench);
    }

    public String getPrefix() {
        return TextFormatting.DARK_PURPLE+"";
    }
}