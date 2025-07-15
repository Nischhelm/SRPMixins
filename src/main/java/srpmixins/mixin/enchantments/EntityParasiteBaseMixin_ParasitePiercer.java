package srpmixins.mixin.enchantments;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.registry.ModEnchantments;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin_ParasitePiercer {
    @ModifyVariable(
            method = "attackEntityFrom",
            at = @At(value = "LOAD", ordinal = 1),
            name = "flagCap"
    )
    private boolean srpmixins_applyIgnoreDmgCapEnchantment(
            boolean value,
            @Local(name = "naniDesu") byte damageType,
            @Local(argsOnly = true) DamageSource source
    ) {
        if (!value || damageType != 1) return value;

        EntityPlayer player = (EntityPlayer) source.getTrueSource();
        ItemStack mainhand = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.parasitePiercer, mainhand);
        if (level <= 0) return true;

        //20% chance per lvl to ignore dmg cap
        return player.getRNG().nextFloat() >= level * SRPMixinsConfigHandler.enchantments.piercer.ignoreChance;
    }

}