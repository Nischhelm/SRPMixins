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
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.rules.conditions.NodeCountCondition;
import srpmixins.rules.ruleset.VariantDisableRuleSet;
import srpmixins.rules.rule.VariantDisableRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;evolutionParasiteAlwaysVariant:B", remap = false)
    )
    private byte srpmixins_ignoreOriginalHandling(byte original){
        if(VariantDisableRuleSet.INSTANCE.hasNoRules()) return original; //Soft-disable mixin if no rules
        return (byte) (SRPConfigProvider.getMaxPhase() + 1);
    }

    @ModifyExpressionValue(
            method = "onInitialSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;variantChance:D", remap = false)
    )
    private double srpmixins_changeVariants(double original){
        if(VariantDisableRuleSet.INSTANCE.hasNoRules()) return SRPConfig.variantChance; //Soft-disable mixin if no rules
        if(this.canChangeVariant) return 0; //afaik just for variant staff but idk
        if(this.phaseCreated < SRPConfigSystems.evolutionParasiteAlwaysVariant && this.getRNG().nextFloat() >= SRPConfig.variantChance) return 0; //normal type, no variant

        int paraId = this.getParasiteIDRegister();

        String mobName = SRPMobConfigProvider.paraIdToMobName.get(paraId);
        List<VariantDisableRule.EnumVariant> availableVariants = new ArrayList<>(SRPMobConfigProvider.mobNameToVariantsMap.get(mobName));
        if(availableVariants.isEmpty()) return SRPConfig.variantChance; //This shouldn't happen but if there are no registered variants, just let SRP do its thing

        Map<String, Object> actualValues = new HashMap<>();
        actualValues.put("dim", this.world.provider.getDimension());
        actualValues.put("phase", this.phaseCreated);
        actualValues.put("mob", paraId);
        actualValues.put("group", SRPMobConfigProvider.getParaGroup(paraId));
        actualValues.put("nodes", SRPWorldData.get(this.world).getNodes("x").size());

        VariantDisableRule.EnumVariant chosenVariant = VariantDisableRuleSet.INSTANCE.getRandomVariant(actualValues, availableVariants, this.getRNG());
        if(chosenVariant == null) return 0; //all variants disabled

        //Moved to Stat Increase Rules
//        if(chosenVariant.skinId == 1) {
//            switch (paraId){
//                case 88: //carrier_colony
//                    this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier("ARMORED", +0.5, 2));
//                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("ARMORED", -0.75, 2));
//                    break;
//                case 51: //ada_longarms
//                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("TYRANT", +1, 2));
//                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("TYRANT", -0.5, 2));
//                    this.setHealth((float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
//                    break;
//                case 10: case 80: case 84: case 87: //pri_reeker, thrall, monarch, haunter
//                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("SPECIAL", +0.5, 2));
//                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("SPECIAL", -0.5, 2));
//                    this.setHealth((float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
//                    break;
//            }
//        }

        this.setSkin(chosenVariant.skinId);
        return 0; //Original code will not run
    }
}
