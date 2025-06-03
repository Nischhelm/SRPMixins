package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import srpmixins.config.SRPMixinsConfigProvider;

import java.util.UUID;

@Mixin(EntityParasiteBase.class)
public abstract class DimensionStatMultiplier extends EntityMob {
    @Unique private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("554f3929-4194-4ae5-a4da-4b528a89ca32");
    @Unique private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("554f3929-4195-4ae5-a4da-4b528a89ca32");
    @Unique private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("554f3929-4196-4ae5-a4da-4b528a89ca32");
    @Unique private static final UUID KBRES_MODIFIER_UUID = UUID.fromString("554f3929-4197-4ae5-a4da-4b528a89ca32");

    public DimensionStatMultiplier(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        if (world.isRemote) return;
        float hp_multiplier = SRPMixinsConfigProvider.dimensionHealthMultipliers.getOrDefault(dimension, 1F) - 1F;
        float dmg_multiplier = SRPMixinsConfigProvider.dimensionDmgMultipliers.getOrDefault(dimension, 1F) - 1F;
        float armor_multiplier = SRPMixinsConfigProvider.dimensionArmorMultipliers.getOrDefault(dimension, 1F) - 1F;
        float kbres_multiplier = SRPMixinsConfigProvider.dimensionKBResMultipliers.getOrDefault(dimension, 1F) - 1F;
        //multiplier--;    //op2 uses x(1+multiplier), so need to -1

        if (Math.abs(hp_multiplier) > 1e-3) {  //if it's close to 0 (x1) we don't need to bother
            AttributeModifier modifierHealth = new AttributeModifier(HEALTH_MODIFIER_UUID, "SRPMixins Health", hp_multiplier, 2);
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(modifierHealth);
        }

        if (Math.abs(dmg_multiplier) > 1e-3) {  //if it's close to 0 (x1) we don't need to bother
            AttributeModifier modifierDamage = new AttributeModifier(DAMAGE_MODIFIER_UUID, "SRPMixins Damage", dmg_multiplier, 2);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(modifierDamage);
        }

        if (Math.abs(armor_multiplier) > 1e-3) {  //if it's close to 0 (x1) we don't need to bother
            AttributeModifier modifierArmor = new AttributeModifier(ARMOR_MODIFIER_UUID, "SRPMixins Armor", armor_multiplier, 2);
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(modifierArmor);
        }

        if (Math.abs(kbres_multiplier) > 1e-3) {  //if it's close to 0 (x1) we don't need to bother
            AttributeModifier modifierKBResistance = new AttributeModifier(KBRES_MODIFIER_UUID, "SRPMixins KB Resistance", kbres_multiplier, 2);
            this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(modifierKBResistance);
        }
    }
}