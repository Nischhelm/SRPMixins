package srpmixins.rules.rulesets;

import srpmixins.SRPMixins;
import srpmixins.rules.basicrules.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DespawnTimerRuleSet extends GenericRuleSet{
    public DespawnTimerRuleSet(String rule) {
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericRule<?>>> getRuleConstructors() {
        return new HashMap<String, Function<String, ? extends GenericRule<?>>>(){{
            put("phase", PhaseRule::new);
            put("dim", DimensionRule::new);
            put("mob", ParaIdRule::new);
            put("group", ParaGroupRule::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        this.timerAdd = 0;

        if(remainingEntries.size() != 1) SRPMixins.LOGGER.warn("SRPMixins unable to parse Despawn Timer Rule, no timer value");
        else {
            try {
                this.timerAdd = Integer.parseInt(remainingEntries.get(0).trim());
            } catch (Exception e) {
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Despawn Timer Rule {}", remainingEntries.get(0));
            }
        }
    }

    private int timerAdd;

    public int getTimer(Map<String, Object> actualValues){
        if(this.timerAdd == 0) return 0;
        if(anyMismatch(actualValues)) return 0;
        return this.timerAdd;
    }
}
