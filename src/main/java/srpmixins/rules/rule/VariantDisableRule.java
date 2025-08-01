package srpmixins.rules.rule;

import srpmixins.SRPMixins;
import srpmixins.rules.conditions.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VariantDisableRule extends GenericRule {
    public VariantDisableRule(String rule) {
        super(rule);
    }

    @Override
    protected Map<String, Function<String, ? extends GenericCondition<?>>> getConditionConstructors() {
        return new HashMap<String, Function<String, ? extends GenericCondition<?>>>(){{
            put("phase", EvoPhaseCondition::new);
            put("dim", DimensionCondition::new);
            put("mob", ParaIdCondition::new);
            put("group", ParaGroupCondition::new);
        }};
    }

    @Override
    protected void parseRemainingConfigEntries(List<String> remainingEntries) {
        this.variantsToDisable = new HashSet<>();

        if (remainingEntries.size() != 1){
            SRPMixins.LOGGER.warn("SRPMixins unable to parse Variant Disable Rule, no variant to disable");
            return;
        }
        String s = remainingEntries.get(0).trim();
        if(!s.startsWith("variant")){
            SRPMixins.LOGGER.warn("SRPMixins unable to parse Variant Disable Rule, no variant to disable in {}", s);
            return;
        }
        s = s.replaceFirst("variant *=", "").trim();

        try {
            this.variantsToDisable.addAll(Arrays
                    .stream(s.split(" +"))
                    .map(EnumVariant::valueOf)
                    .collect(Collectors.toSet()));
        } catch (Exception e) {
            SRPMixins.LOGGER.warn("SRPMixins unable to parse Variant Rule entry {}", s);
        }
    }

    private Set<EnumVariant> variantsToDisable;

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
