package srpmixins.rules.basicrules;

import java.util.Map;

public abstract class GenericRule<T> {

    protected T comparisonValue;
    protected String key;

    public GenericRule(String key, String s){
        this.key = key;
        this.comparisonValue = parseConfigInput(s.replaceFirst(key, "").trim());
    }

    public abstract T parseConfigInput(String s);

    public boolean test(Map<String, Object> actualValues) {
        return actualValues.get(this.key).equals(comparisonValue);
    }
}
