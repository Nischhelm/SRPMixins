package srpmixins.rules.ruleset;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rule.MinMaxDayPerPhaseRule;

import java.util.Map;

public class MinMaxDayPerPhaseRuleSet extends GenericRuleSet<MinMaxDayPerPhaseRule> {
    public static MinMaxDayPerPhaseRuleSet INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.minMaxDaysPerPhase;
    }

    @Override
    public MinMaxDayPerPhaseRule constructRule(String configLine) {
        return new MinMaxDayPerPhaseRule(configLine);
    }

    public int getTotalMin(Map<String, Object> actualValues){
        int min = Integer.MAX_VALUE;
        for(MinMaxDayPerPhaseRule rule : allRules) min = Math.min(min, rule.getMin(actualValues));
        return min;
    }

    public int getTotalMax(Map<String, Object> actualValues){
        int max = 0;
        for(MinMaxDayPerPhaseRule rule : allRules) max = Math.max(max, rule.getMax(actualValues));
        return max;
    }
}
