package srpmixins.rules.rulesetholder;

import srpmixins.rules.rulesets.GenericRuleSet;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericRuleSetHolder<T extends GenericRuleSet> {
    protected final List<T> allRules = new ArrayList<>();

    public void reset() {
        this.allRules.clear();
        init();
    }

    public void init() {
        for(String s : getConfigEntry()) this.allRules.add(constructRuleSet(s));
    }

    public boolean hasNoRules() {
        return this.allRules.isEmpty();
    }

    public abstract String[] getConfigEntry();
    public abstract T constructRuleSet(String configLine);

    public GenericRuleSetHolder(){
        init();
    }
}
