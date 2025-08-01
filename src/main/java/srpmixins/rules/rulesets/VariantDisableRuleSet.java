package srpmixins.rules.rulesets;

import srpmixins.SRPMixins;
import srpmixins.rules.basicrules.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VariantDisableRuleSet extends GenericRuleSet {
    public VariantDisableRuleSet(String rule) {
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericRule<?>>> getRuleConstructors() {
        return new HashMap<String, Function<String, ? extends GenericRule<?>>>(){{
            put("phase", PhaseRule::new);
            put("dim", DimensionRule::new);
            put("mob", ParaIdRule::new);
            put("group", ParaGroupRule::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        for(String s : remainingEntries){
            try {
                if (s.contains("variant"))
                    variantsToDisable.addAll(Arrays.stream(s.replaceFirst("variant *= *", "").trim().split(" +")).map(EnumVariant::valueOf).collect(Collectors.toList()));
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse Variant Rule {}", s);
            }
        }
    }

    private final Set<EnumVariant> variantsToDisable = new HashSet<>();

    public void disableVariants(List<EnumVariant> availableVariants, Map<String, Object> actualValues) {
        if(this.variantsToDisable.isEmpty() || availableVariants.stream().noneMatch(this.variantsToDisable::contains)) return;
        if(anyMismatch(actualValues)) return;

        availableVariants.removeAll(this.variantsToDisable);
    }
    
    public enum EnumVariant {
        SPECIAL(1),
        VIRULENT(5),
        BERSERKER(6),
        BREACHER(7);

        public final int skinId;

        EnumVariant(int skinId){
            this.skinId = skinId;
        }
    }
}
