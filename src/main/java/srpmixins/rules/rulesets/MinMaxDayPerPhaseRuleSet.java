package srpmixins.rules.rulesets;

import srpmixins.SRPMixins;
import srpmixins.rules.basicrules.DimensionRule;
import srpmixins.rules.basicrules.GenericRule;
import srpmixins.rules.basicrules.PhaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MinMaxDayPerPhaseRuleSet extends GenericRuleSet {
    public MinMaxDayPerPhaseRuleSet(String rule){
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericRule<?>>> getRuleConstructors() {
        return new HashMap<String, Function<String, ? extends GenericRule<?>>>() {{
            put("phase", PhaseRule::new);
            put("dim", DimensionRule::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        for(String s : remainingEntries){
            try {
                if (s.contains("min")) min = Integer.parseInt(s.replaceFirst("min *=", "").trim());
                else if (s.contains("max")) max = Integer.parseInt(s.replaceFirst("max *=", "").trim());

                if (min != Integer.MAX_VALUE && max != 0 && min > max) max = min; //If min > max, set them equal
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Days Per Phase Rule {}", s);
            }
        }
    }

    private int min = Integer.MAX_VALUE;
    private int max = 0;

    public int getMin(Map<String, Object> actualValues){
        if(anyMismatch(actualValues)) return Integer.MAX_VALUE;
        return min;
    }

    public int getMax(Map<String, Object> actualValues){
        if(anyMismatch(actualValues)) return 0;
        return max;
    }
}
