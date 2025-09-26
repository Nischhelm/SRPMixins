package srpmixins.rules.rule;

import srpmixins.SRPMixins;
import srpmixins.rules.conditions.DimensionCondition;
import srpmixins.rules.conditions.EvoPhaseCondition;
import srpmixins.rules.conditions.GenericCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MinMaxDayPerPhaseRule extends GenericRule {
    public MinMaxDayPerPhaseRule(String rule){
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericCondition<?>>> getConditionConstructors() {
        return new HashMap<String, Function<String, ? extends GenericCondition<?>>>() {{
            put("phase", EvoPhaseCondition::new);
            put("dim", DimensionCondition::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        this.min = Integer.MAX_VALUE;
        this.max = 0;

        for(String s : remainingEntries){
            try {
                if (s.contains("min")) this.min = Integer.parseInt(s.replaceFirst("min *=", "").trim());
                else if (s.contains("max")) this.max = Integer.parseInt(s.replaceFirst("max *=", "").trim());

                if (this.min != Integer.MAX_VALUE && this.max != 0 && this.min > this.max)
                    this.max = this.min; //If min > max, set them equal
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Days Per Phase Rule {}", s);
            }
        }
    }

    private int min;
    private int max;

    public int getMin(Map<String, Object> actualValues){
        if(this.min == Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if(anyMismatch(actualValues)) return Integer.MAX_VALUE;
        return this.min;
    }

    public int getMax(Map<String, Object> actualValues){
        if(this.max == 0) return 0;
        if(anyMismatch(actualValues)) return 0;
        return this.max;
    }
}
