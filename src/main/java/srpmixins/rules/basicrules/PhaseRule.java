package srpmixins.rules.basicrules;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;

import java.util.Map;

public class PhaseRule extends GenericRule<Byte> {

    private EnumOperation operation;

    public PhaseRule(String s){
        super("phase", s);
    }

    @Override
    public Byte parseConfigInput(String s){
        String opSign = s.replaceAll("\\d+", "").trim();
        this.operation = EnumOperation.getBySign(opSign);

        return Byte.parseByte(s.replaceAll("[!><=]", "").trim());
    }

    @Override
    public boolean test(Map<String, Object> actualValues) {
        return SRPConfigSystems.useEvolution && this.operation.evaluate((byte) actualValues.get(this.key), comparisonValue);
    }
}
