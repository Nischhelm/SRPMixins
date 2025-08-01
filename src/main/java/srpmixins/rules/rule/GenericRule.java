package srpmixins.rules.rule;

import srpmixins.SRPMixins;
import srpmixins.rules.conditions.GenericCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class GenericRule {
    protected final List<GenericCondition<?>> conditions = new ArrayList<>();

    protected abstract Map<String, Function<String, ? extends GenericCondition<?>>> getConditionConstructors();

    public GenericRule(String rule) {
        rule = rule.replaceAll("\\[", "");
        String[] split = rule.split("[;,\\]]"); //allows to also use , or ; in config

        List<String> remainingEntries = new ArrayList<>();

        Map<String, Function<String, ? extends GenericCondition<?>>> constructorMap = getConditionConstructors();

        splitLoop:
        for (String s : split) {
            s = s.trim();

            for (Map.Entry<String, Function<String, ? extends GenericCondition<?>>> constructorMapEntry : constructorMap.entrySet()) {
                if (s.startsWith(constructorMapEntry.getKey())) {
                    try {
                        conditions.add(constructorMapEntry.getValue().apply(s));
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
        for(GenericCondition<?> rule : conditions) if(!rule.test(actualValues)) return true;
        return false;
    }
}
