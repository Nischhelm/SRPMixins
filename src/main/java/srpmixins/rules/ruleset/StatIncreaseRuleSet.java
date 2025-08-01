package srpmixins.rules.ruleset;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rule.StatIncreaseRule;

import java.util.Map;

public class StatIncreaseRuleSet extends GenericRuleSet<StatIncreaseRule> {
    public static StatIncreaseRuleSet INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.statIncreaseRules;
    }

    @Override
    public StatIncreaseRule constructRule(String configLine) {
        return new StatIncreaseRule(configLine);
    }

    public Multimap<String, AttributeModifier> getAllModifiers(Map<String, Object> actualValues){
        Multimap<String, AttributeModifier> modifiers = HashMultimap.create();
        for(StatIncreaseRule rule : allRules)
            if(rule.hasModifiers(actualValues))
                modifiers.putAll(rule.getModifiers());
        return modifiers;
    }
}
