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

    public GenericRuleSet(String rule){
        rule = rule.replaceAll("\\[", "");
        String[] split = rule.split("]");

        List<String> remainingEntries = new ArrayList<>();
        for(String s : split){
            try {
                for(Map.Entry<String, Function<String, ? extends GenericRule<?>>> constructors : getRuleConstructors().entrySet()){
                    if(s.contains(constructors.getKey())){
                        rules.add(constructors.getValue().apply(s));
                        break;
                    }
                    remainingEntries.add(s);
                }
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Rule {}", s);
            }
        }

        parseRemainingConfigEntries(remainingEntries);
    }

    protected abstract void parseRemainingConfigEntries(List<String> remainingEntries);

    protected boolean anyMismatch(Map<String, Object> actualValues){
        for(GenericRule<?> rule : rules) if(!rule.test(actualValues)) return false;
        return true;
    }
}
