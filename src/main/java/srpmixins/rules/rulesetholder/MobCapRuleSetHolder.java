package srpmixins.rules.rulesetholder;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rulesets.MobCapRuleSet;

import java.util.HashMap;
import java.util.Map;

public class MobCapRuleSetHolder extends GenericRuleSetHolder<MobCapRuleSet>{
    public static MobCapRuleSetHolder INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.mobCapRules;
    }

    @Override
    public MobCapRuleSet constructRuleSet(String configLine) {
        return new MobCapRuleSet(configLine);
    }

    public double getTotalMulti(int dimId, byte phase){
        Map<String, Object> actualValues = new HashMap<>();
        actualValues.put("dim", dimId);
        actualValues.put("phase", phase);

        double totalMulti = 1;
        for(MobCapRuleSet rule : allRules) totalMulti *= rule.getMulti(actualValues);
        return totalMulti;
    }
}
