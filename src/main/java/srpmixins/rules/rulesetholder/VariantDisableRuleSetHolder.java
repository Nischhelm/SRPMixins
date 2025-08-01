package srpmixins.rules.rulesetholder;

import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.rules.rulesets.VariantDisableRuleSet;

import java.util.*;

public class VariantDisableRuleSetHolder extends GenericRuleSetHolder<VariantDisableRuleSet>{
    public static VariantDisableRuleSetHolder INSTANCE;

    @Override
    public String[] getConfigEntry() {
        return SRPMixinsConfigHandler.rules.variantRules;
    }

    @Override
    public VariantDisableRuleSet constructRuleSet(String configLine) {
        return new VariantDisableRuleSet(configLine);
    }

    public VariantDisableRuleSet.EnumVariant getRandomVariant(int paraId, int dimId, byte currPhase, Random rand) {
        String mobName = SRPMobConfigProvider.paraIdToMobName.get(paraId);
        String group = SRPMobConfigProvider.getParaGroup(paraId);

        List<VariantDisableRuleSet.EnumVariant> availableVariants = new ArrayList<>(SRPMobConfigProvider.mobNameToVariantsMap.get(mobName));
        if(availableVariants.isEmpty()) return null;

        Map<String, Object> actualValues = new HashMap<>();
        actualValues.put("dim", dimId);
        actualValues.put("phase", currPhase);
        actualValues.put("mob", paraId);
        actualValues.put("group", group);

        for(VariantDisableRuleSet rule : allRules) rule.disableVariants(availableVariants, actualValues);

        if(availableVariants.isEmpty()) return null;
        return availableVariants.get(rand.nextInt(availableVariants.size()));
    }
}
