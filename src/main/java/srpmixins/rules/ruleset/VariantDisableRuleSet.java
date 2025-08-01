package srpmixins.rules.ruleset;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rule.VariantDisableRule;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class VariantDisableRuleSet extends GenericRuleSet<VariantDisableRule> {
    public static VariantDisableRuleSet INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.variantRules;
    }

    @Override
    public VariantDisableRule constructRule(String configLine) {
        return new VariantDisableRule(configLine);
    }

    public VariantDisableRule.EnumVariant getRandomVariant(Map<String, Object> actualValues, List<VariantDisableRule.EnumVariant> availableVariants, Random rand) {
        for(VariantDisableRule rule : allRules) rule.disableVariants(availableVariants, actualValues);

        if(availableVariants.isEmpty()) return null;
        return availableVariants.get(rand.nextInt(availableVariants.size()));
    }
}
