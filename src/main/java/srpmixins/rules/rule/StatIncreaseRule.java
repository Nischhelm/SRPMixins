package srpmixins.rules.rule;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import srpmixins.SRPMixins;
import srpmixins.rules.conditions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class StatIncreaseRule extends GenericRule {
    private static int statCounter = 0;

    public StatIncreaseRule(String rule) {
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericCondition<?>>> getConditionConstructors() {
        return new HashMap<String, Function<String, ? extends GenericCondition<?>>>(){{
            put("phase", EvoPhaseCondition::new);
            put("dim", DimensionCondition::new);
            put("bloodmoon", BloodMoonCondition::new);
            put("mob", ParaIdCondition::new);
            put("group", ParaGroupCondition::new);
            put("variant", VariantCondition::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        if(remainingEntries.isEmpty()){
            SRPMixins.LOGGER.warn("SRPMixins unable to parse Stat Increase Rule, no stat modifiers given.");
            return;
        }

        this.modifiers = HashMultimap.create();

        for(String s : remainingEntries){
            String[] split = s.split("=");
            if(split.length != 2){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Stat Increase Rule Modifier, didn't name modifying value in {}", s);
                continue;
            }
            String attributeName = split[0].trim();
            if(attributeAliases.containsKey(attributeName))
                attributeName = attributeAliases.get(attributeName);

            try {
                int operation = 2;
                double value;
                if (split[1].contains("@")) { //Pattern: value @ operation
                    String[] modOp = split[1].trim().split("@");

                    String opString = modOp[1].trim();
                    if(operationAliases.containsKey(opString))
                        operation = operationAliases.get(opString);
                    else {
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse Stat Increase Rule Modifier, operation needs to be 0 (ADD) 1 (MULT_BASE) or 2 (MULT_TOTAL)");
                        continue;
                    }

                    value = Double.parseDouble(modOp[0].trim());
                    if(value == 0) continue; //no need to do anything for unchanging stats
                } else
                    value = Double.parseDouble(split[1].trim());

                if (operation == 1 || operation == 2) value -= 1; //op1/op2 modifiers are used as x(1 + value)

                AttributeModifier mod = new AttributeModifier(
                        UUID.nameUUIDFromBytes(("srpmixins_uuid" + (statCounter++)).getBytes()),
                        "SRPMIXINS_STAT",
                        value,
                        operation
                );

                this.modifiers.put(attributeName, mod);
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Stat Increase Rule Modifier, parsing number(s) failed in {}", s);
            }
        }
    }

    private Multimap<String, AttributeModifier> modifiers;
    public boolean hasModifiers(Map<String, Object> actualValues) {
        return !this.modifiers.isEmpty() && !anyMismatch(actualValues);
    }
    public Multimap<String, AttributeModifier> getModifiers() {
        return this.modifiers;
    }

    private static final Map<String, String> attributeAliases = new HashMap<String, String>(){{
        put("DMG", "generic.attackDamage");
        put("DAMAGE", "generic.attackDamage");
        put("ATTACKDAMAGE", "generic.attackDamage");
        put("ATTACK", "generic.attackDamage");
        put("ATK", "generic.attackDamage");

        put("ATKSPD", "generic.attackSpeed");
        put("ATKSPEED", "generic.attackSpeed");
        put("ATTACKSPD", "generic.attackSpeed");
        put("ATTACKSPEED", "generic.attackSpeed");

        put("HP", "generic.maxHealth");
        put("MAXHP", "generic.maxHealth");
        put("HEALTH", "generic.maxHealth");
        put("MAXHEALTH", "generic.maxHealth");

        put("KB", "generic.knockbackResistance");
        put("KBRES", "generic.knockbackResistance");
        put("KBRESISTANCE", "generic.knockbackResistance");

        put("ARMOR", "generic.armor");

        put("TOUGH", "generic.armorToughness");
        put("TOUGHNESS", "generic.armorToughness");
        put("ARMORTOUGHNESS", "generic.armorToughness");

        put("SPD", "generic.movementSpeed");
        put("SPEED", "generic.movementSpeed");
        put("MOVSPEED", "generic.movementSpeed");
        put("MOVEMENTSPEED", "generic.movementSpeed");

        put("LUCK", "generic.luck");

        put("RANGE", "generic.followRange");
        put("FOLLOW", "generic.followRange");
        put("FOLLOWRANGE", "generic.followRange");
    }};

    private static final Map<String, Integer> operationAliases = new HashMap<String, Integer>(){{
        put("0", 0);
        put("ADD", 0);
        put("ADDITION", 0);
        put("+", 0);
        put("op0", 0);
        put("OP0", 0);
        put("OPERATION0", 0);

        put("1", 1);
        put("%", 1);
        put("MULT_BASE", 1);
        put("op1", 1);
        put("OP1", 1);
        put("OPERATION1", 1);

        put("2", 2);
        put("*", 2);
        put("x", 2);
        put("MULT", 2);
        put("MULT_TOTAL", 2);
        put("op2", 2);
        put("OP2", 2);
        put("OPERATION2", 2);
    }};
}
