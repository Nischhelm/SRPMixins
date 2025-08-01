package srpmixins.rules.rulesetholder;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rulesets.DespawnTimerRuleSet;

import java.util.HashMap;
import java.util.Map;

public class DespawnTimerRuleSetHolder extends GenericRuleSetHolder<DespawnTimerRuleSet>{
    public static DespawnTimerRuleSetHolder INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.despawnTimerRules;
    }

    @Override
    public DespawnTimerRuleSet constructRuleSet(String configLine) {
        return new DespawnTimerRuleSet(configLine);
    }

    public int getTotalTimer(int dimId, byte phase, int paraId, String group){
        Map<String, Object> actualValues = new HashMap<>();
        actualValues.put("dim", dimId);
        actualValues.put("phase", phase);
        actualValues.put("mob", paraId);
        actualValues.put("group", group);

        int timer = 0;
        for(DespawnTimerRuleSet rule : allRules) timer += rule.getTimer(actualValues);
        return timer;
    }
}
