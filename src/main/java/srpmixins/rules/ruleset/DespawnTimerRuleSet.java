package srpmixins.rules.ruleset;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rule.DespawnTimerRule;

import java.util.Map;

public class DespawnTimerRuleSet extends GenericRuleSet<DespawnTimerRule> {
    public static DespawnTimerRuleSet INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.despawnTimerRules;
    }

    @Override
    public DespawnTimerRule constructRule(String configLine) {
        return new DespawnTimerRule(configLine);
    }

    public int getTotalTimer(Map<String, Object> actualValues) {
        int timer = 0;
        for(DespawnTimerRule rule : allRules) timer += rule.getTimer(actualValues);
        return timer;
    }
}
