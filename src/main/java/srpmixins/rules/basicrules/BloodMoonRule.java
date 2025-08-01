package srpmixins.rules.basicrules;

import srpmixins.compat.BloodMoonCompat;
import srpmixins.compat.CompatUtil;

import java.util.Map;

public class BloodMoonRule extends GenericRule<Boolean> {

    public BloodMoonRule(String s){
        super("bloodmoon", s);
    }

    @Override
    public Boolean parseConfigInput(String s){
        return Boolean.parseBoolean(s.replaceFirst("=", "").trim());
    }

    @Override
    public boolean test(Map<String, Object> actualValues) {
        boolean actualValue = CompatUtil.bloodMoon.isLoaded() && BloodMoonCompat.isBloodMoonActive();
        return actualValue == comparisonValue;
    }
}
