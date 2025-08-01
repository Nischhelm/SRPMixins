package srpmixins.rules.rulesets;

import srpmixins.SRPMixins;
import srpmixins.rules.basicrules.GenericRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class GenericRuleSet {
    protected final List<GenericRule<?>> rules = new ArrayList<>();

    protected abstract Map<String, Function<String, ? extends GenericRule<?>>> getRuleConstructors();

    public GenericRuleSet(String rule) {
        rule = rule.replaceAll("\\[", "");
        String[] split = rule.split("[;,\\]]"); //allows to also use , or ; in config

        List<String> remainingEntries = new ArrayList<>();

        splitLoop:
        for (String s : split) {
            s = s.trim();

            for (Map.Entry<String, Function<String, ? extends GenericRule<?>>> constructorMapEntry : getRuleConstructors().entrySet()) {
                if (s.startsWith(constructorMapEntry.getKey())) {
                    try {
                        rules.add(constructorMapEntry.getValue().apply(s));
                        continue splitLoop;
                    } catch (Exception e) {
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse Rule {}", s);
                    }
                }
            }

            remainingEntries.add(s);
        }

        parseRemainingConfigEntries(remainingEntries);
    }

    protected abstract void parseRemainingConfigEntries(List<String> remainingEntries);

    protected boolean anyMismatch(Map<String, Object> actualValues){
        for(GenericRule<?> rule : rules) if(!rule.test(actualValues)) return true;
        return false;
    }
}
