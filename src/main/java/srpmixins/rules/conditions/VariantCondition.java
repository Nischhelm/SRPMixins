package srpmixins.rules.conditions;

import srpmixins.rules.rule.VariantDisableRule;

import java.util.Map;

public class VariantCondition extends GenericCondition<VariantDisableRule.EnumVariant> {
    public VariantCondition(String s) {
        super("variant", s);
    }

    @Override
    public VariantDisableRule.EnumVariant parseConfigInput(String s) {
        return VariantDisableRule.EnumVariant.valueOf(s.replaceFirst("=","").trim());
    }

    @Override
    public boolean test(Map<String, Object> actualValues) {
        Integer actualValue = (Integer) actualValues.get(this.key);
        return this.comparisonValue.skinId == actualValue;
    }
}
