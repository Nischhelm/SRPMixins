package srpmixins.rules.ruleset;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rule.MobCapRule;

import java.util.Map;

public class MobCapRuleSet extends GenericRuleSet<MobCapRule> {
    public static MobCapRuleSet INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.mobCapRules;
    }

    @Override
    public MobCapRule constructRule(String configLine) {
        return new MobCapRule(configLine);
    }

    public double getTotalMulti(Map<String, Object> actualValues){
        double totalMulti = 1;
        for(MobCapRule rule : allRules) totalMulti *= rule.getMulti(actualValues);
        return totalMulti;
    }
}
