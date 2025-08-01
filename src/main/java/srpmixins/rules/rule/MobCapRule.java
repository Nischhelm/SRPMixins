package srpmixins.rules.rule;

import srpmixins.SRPMixins;
import srpmixins.rules.conditions.BloodMoonCondition;
import srpmixins.rules.conditions.DimensionCondition;
import srpmixins.rules.conditions.GenericCondition;
import srpmixins.rules.conditions.EvoPhaseCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MobCapRule extends GenericRule {
    public MobCapRule(String rule){
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericCondition<?>>> getRuleConstructors() {
        return new HashMap<String, Function<String, ? extends GenericCondition<?>>>(){{
            put("phase", EvoPhaseCondition::new);
            put("dim", DimensionCondition::new);
            put("bloodmoon", BloodMoonCondition::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        this.multi = 1;

        if(remainingEntries.size() != 1) SRPMixins.LOGGER.warn("SRPMixins unable to parse Mob Cap Rule, no multiplier");
        else {
            try {
                this.multi = Double.parseDouble(remainingEntries.get(0).trim());
            } catch (Exception e) {
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Mob Cap Rule {}", remainingEntries.get(0));
            }
        }
    }

    private double multi;

    public double getMulti(Map<String, Object> actualValues) {
        if(this.multi == 1) return 1;
        if(anyMismatch(actualValues)) return 1;
        return this.multi;
    }
}
