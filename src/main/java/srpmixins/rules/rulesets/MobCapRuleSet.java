package srpmixins.rules.rulesets;

import srpmixins.SRPMixins;
import srpmixins.rules.basicrules.BloodMoonRule;
import srpmixins.rules.basicrules.DimensionRule;
import srpmixins.rules.basicrules.GenericRule;
import srpmixins.rules.basicrules.PhaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MobCapRuleSet extends GenericRuleSet {
    public MobCapRuleSet(String rule){
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericRule<?>>> getRuleConstructors() {
        return new HashMap<String, Function<String, ? extends GenericRule<?>>>(){{
            put("phase", PhaseRule::new);
            put("dim", DimensionRule::new);
            put("bloodmoon", BloodMoonRule::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        if(remainingEntries.size() != 1) SRPMixins.LOGGER.warn("SRPMixins unable to parse Mob Cap Rule, no multiplier");
        else {
            try {
                multi = Double.parseDouble(remainingEntries.get(0).trim());
            } catch (Exception e) {
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Mob Cap Rule {}", remainingEntries.get(0));
            }
        }
    }

    private double multi = 1;

    public double getMulti(Map<String, Object> actualValues){
        if(anyMismatch(actualValues)) return 1;
        return multi;
    }
}
