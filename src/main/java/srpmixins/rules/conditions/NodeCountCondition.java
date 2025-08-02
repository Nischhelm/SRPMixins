package srpmixins.rules.conditions;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;

import java.util.Map;

public class NodeCountCondition extends GenericCondition<Integer> {

    private EnumOperation operation;

    public NodeCountCondition(String s){
        super("nodes", s);
    }

    @Override
    public Integer parseConfigInput(String s){
        String opSign = s.replaceAll("\\d+", "").trim();
        this.operation = EnumOperation.getBySign(opSign);

        return Integer.parseInt(s.replaceAll("[!><=]", "").trim());
    }

    @Override
    public boolean test(Map<String, Object> actualValues) {
        return SRPConfigWorld.nodesActivated && this.operation.evaluate((byte) actualValues.get(this.key), comparisonValue);
    }
}
