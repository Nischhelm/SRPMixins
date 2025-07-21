package srpmixins.rules;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.*;
import java.util.stream.Collectors;

public class VariantRule {
    private static final List<VariantRule> allRules = new ArrayList<>();
    public static void reset(){
        allRules.clear();
        init();
    }
    public static void init(){
        for(String s : SRPMixinsConfigHandler.dmgfix.variantRules)
            allRules.add(new VariantRule(s));
    }
    public static EnumVariant getRandomVariant(int paraId, int dimId, byte currPhase, Random rand) {
        String mobName = SRPMobConfigProvider.paraIdToMobName.get(paraId);
        String group = "";
        for (Map.Entry<String, List<Integer>> entry : SRPMobConfigProvider.mobGroupToParaIdMap.entrySet())
            if (entry.getValue().contains(paraId)) {
                group = entry.getKey();
                break;
            }

        List<EnumVariant> availableVariants = new ArrayList<>(SRPMobConfigProvider.mobNameToVariantsMap.get(mobName));
        if(availableVariants.isEmpty()) return null;

        for(VariantRule rule : allRules) rule.disableVariants(availableVariants, paraId, group, dimId, currPhase);

        if(availableVariants.isEmpty()) return null;
        return availableVariants.get(rand.nextInt(availableVariants.size()));
    }
    public static boolean hasNoRules() {
        return allRules.isEmpty();
    }

    private final Set<EnumVariant> variantsToDisable = new HashSet<>();
    private final List<Integer> mobids = new ArrayList<>();
    private final Map<Integer, EnumOperation> phaseRules = new HashMap<>();
    private Integer dimId = null;
    private String group = null;

    public VariantRule(String rule){
        //Config is nice to write with [..] [..] but for parsing its better to just have rule1, rule2, ... , multi
        rule = rule.replaceAll("\\[", "");
        rule = rule.replaceAll("]", ",");

        String[] split = rule.split(",");
        for(String s : split){
            if(s.contains("phase")){
                s = s.replaceFirst("phase","").trim();

                int phase = Integer.parseInt(s.replaceAll("[><=]","").trim());
                String opSign = s.replaceAll("\\d+","").trim();
                phaseRules.put(phase, EnumOperation.getBySign(opSign));
            }
            else if(s.contains("variant")) variantsToDisable.addAll(Arrays.stream(s.replaceFirst("variant *= *","").trim().split(" +")).map(EnumVariant::valueOf).collect(Collectors.toList()));
            else if(s.contains("mob")) mobids.addAll(Arrays.stream(s.replaceFirst("mob *= *","").trim().split(" +")).map(SRPMobConfigProvider.mobNameToParaIdMap::get).collect(Collectors.toList()));
            else if(s.contains("dim")) dimId = Integer.parseInt(s.replaceFirst("dim *=","").trim());
            else if(s.contains("group")) group = s.replaceFirst("group *=","").trim();
        }
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        if(dimId != null) s.append("[dim = ").append(dimId).append("] ");
        if(!phaseRules.isEmpty()) for (Map.Entry<Integer, EnumOperation> entry : phaseRules.entrySet())
            s.append("[phase ").append(entry.getValue().toString()).append(" ").append(entry.getKey()).append("] ");
        if(!mobids.isEmpty()){
            s.append("[mob =");
            for(int paraId : mobids) s.append(" ").append(SRPMobConfigProvider.paraIdToMobName.get(paraId));
            s.append("] ");
        }
        if(group != null) s.append("[group = ").append(group).append("] ");

        s.append("[variant =");
        for(EnumVariant var : variantsToDisable) s.append(" ").append(var.toString());
        s.append("]");

        return s.toString();
    }

    public void disableVariants(List<EnumVariant> availableVariants, int paraId, String group, int dimId, byte phase) {
        if(this.variantsToDisable.isEmpty() || availableVariants.stream().noneMatch(this.variantsToDisable::contains)) return;
        if(!this.mobids.isEmpty() && !this.mobids.contains(paraId)) return;
        if(this.group != null && !this.group.equals(group)) return;
        if(this.dimId != null && this.dimId != dimId) return;
        if(SRPConfigSystems.useEvolution)
            for(Map.Entry<Integer, EnumOperation> entry : phaseRules.entrySet())
                if(!entry.getValue().evaluate(phase, entry.getKey()))
                    return;

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
