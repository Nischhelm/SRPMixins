package srpmixins.rules.ruleset;

import srpmixins.rules.rule.GenericRule;

import java.util.ArrayList;
import java.util.List;

/**
 * RuleSets are basically wrappers for a config String[] list.
 * They hold Rules, which are wrappers for all conditions and results from a single line in that config list
 * Conditions are just basically predicates that check a given config value against a currently observed value
 */
public abstract class GenericRuleSet<T extends GenericRule> {
    protected final List<T> allRules = new ArrayList<>();

    public void reset() {
        this.allRules.clear();
        init();
    }

    public void init() {
        for(String s : getConfigEntry()) this.allRules.add(constructRule(s));
    }

    public boolean hasNoRules() {
        return this.allRules.isEmpty();
    }

    public abstract String[] getConfigEntry();
    public abstract T constructRule(String configLine);

    public GenericRuleSet(){
        init();
    }
}
