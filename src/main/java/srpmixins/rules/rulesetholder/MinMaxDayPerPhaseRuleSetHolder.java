package srpmixins.rules.rulesetholder;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rulesets.MinMaxDayPerPhaseRuleSet;

import java.util.Map;

public class MinMaxDayPerPhaseRuleSetHolder extends GenericRuleSetHolder<MinMaxDayPerPhaseRuleSet>{
    public static MinMaxDayPerPhaseRuleSetHolder INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.minMaxDaysPerPhase;
    }

    @Override
    public MinMaxDayPerPhaseRuleSet constructRuleSet(String configLine) {
        return new MinMaxDayPerPhaseRuleSet(configLine);
    }

    public int getTotalMin(Map<String, Object> actualValues){
        int min = Integer.MAX_VALUE;
        for(MinMaxDayPerPhaseRuleSet rule : allRules) min = Math.min(min, rule.getMin(actualValues));
        return min;
    }

    public int getTotalMax(Map<String, Object> actualValues){
        int max = 0;
        for(MinMaxDayPerPhaseRuleSet rule : allRules) max = Math.max(max, rule.getMax(actualValues));
        return max;
    }
}
