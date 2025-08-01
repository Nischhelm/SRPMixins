package srpmixins.rules.conditions;

import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParaIdCondition extends GenericCondition<Set<Integer>> {

    public ParaIdCondition(String s){
        super("mob", s);
    }

    @Override
    public Set<Integer> parseConfigInput(String s){
        return Arrays.stream(s.replaceFirst("=", "").trim().split(" +")).map(SRPMobConfigProvider.mobNameToParaIdMap::get).collect(Collectors.toSet());
    }

    @Override
    public boolean test(Map<String, Object> actualValues) {
        Integer actualValue = (Integer) actualValues.get(this.key);
        return this.comparisonValue.contains(actualValue);
    }
}
