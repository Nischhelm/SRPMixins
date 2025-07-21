package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.*;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityMes;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.*;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.*;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.*;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityVesta;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.rules.VariantRule;

@Mixin(value = {
        EntityBanoAdapted.class,
        EntityCanraAdapted.class,
        EntityEmanaAdapted.class,
        EntityHullAdapted.class,
        EntityNoglaAdapted.class,
        EntityRanracAdapted.class,
        EntityShycoAdapted.class,
        EntityBano.class,
        EntityCanra.class,
        EntityEmana.class,
        EntityHull.class,
        EntityNogla.class,
        EntityRanrac.class,
        EntityShyco.class,
        EntityLum.class,
        EntityMes.class,
        EntityButhol.class,
        EntityGothol.class,
        EntityMudo.class,
        EntityNuuh.class,
        EntityRathol.class,
        EntityInfEnderman.class,
        EntityAlafha.class,
        EntityAnged.class,
        EntityEsor.class,
        EntityFlog.class,
        EntityGanro.class,
        EntityOmboo.class,
        EntityOrch.class,
        EntityPheon.class,
        EntityVesta.class
})
public abstract class VariantMixin extends EntityParasiteBase {
    public VariantMixin(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) public abstract int getParasiteIDRegister();

    @ModifyExpressionValue(
            method = "onInitialSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;variantChance:D", remap = false)
    )
    private double srpmixins_changeVariants(double original){
        if(this.canChangeVariant) return SRPConfig.variantChance;
        if(VariantRule.hasNoRules()) return SRPConfig.variantChance;
        if(this.phaseCreated < SRPConfigSystems.evolutionParasiteAlwaysVariant && this.getRNG().nextFloat() >= SRPConfig.variantChance) return 0;

        int paraId = this.getParasiteIDRegister();
        VariantRule.EnumVariant variant = VariantRule.getRandomVariant(paraId, this.world.provider.getDimension(), this.phaseCreated, this.getRNG());
        if(variant == null) return 0; //all variants disabled

        if(variant.skinId == 1) {
            switch (paraId){
                case 88: //carrier_colony
                    this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier("ARMORED", +0.5, 2));
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("ARMORED", -0.75, 2));
                    break;
                case 51: //ada_longarms
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("TYRANT", +1, 2));
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("TYRANT", -0.5, 2));
                    this.setHealth((float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
                    break;
                case 10: case 80: case 84: case 87: //pri_reeker, thrall, monarch, haunter
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("SPECIAL", +0.5, 2));
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("SPECIAL", -0.5, 2));
                    this.setHealth((float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
                    break;
            }
        }

        this.setSkin(variant.skinId);
        return 0;
    }
}
