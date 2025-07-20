package srpmixins.rules;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.compat.BloodMoonCompat;
import srpmixins.compat.CompatUtil;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobCapRule {
    private static final List<MobCapRule> allRules = new ArrayList<>();
    public static double getTotalMulti(int dimId, byte phase){
        double multi = 1;
        for(MobCapRule rule : allRules) multi *= rule.getMulti(dimId, phase);
        return multi;
    }
    public static void reset(){
        allRules.clear();
        init();
    }
    public static void init(){
        for(String s : SRPMixinsConfigHandler.spawns.mobCapRules)
            allRules.add(new MobCapRule(s));
    }

    private Boolean bloodMoonRule = null;
    private Integer dimRule = null;
    private final Map<Integer, EnumOperation> phaseRules = new HashMap<>();
    private double multi = 1;

    public MobCapRule(String rule){
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
            else if(s.contains("dim")) dimRule = Integer.parseInt(s.replaceFirst("dim *=","").trim());
            else if(s.contains("bloodmoon")) bloodMoonRule = Boolean.parseBoolean(s.replaceFirst("bloodmoon *=","").trim());
            else multi = Double.parseDouble(s.trim());
        }
    }

    public double getMulti(int dimId, byte phase){
        if(dimRule != null && dimId != dimRule) return 1;
        if(bloodMoonRule != null && (!CompatUtil.isBloodMoonLoaded() || BloodMoonCompat.isBloodMoonActive() != bloodMoonRule)) return 1; //Ignores all lines that have bloodmoon mention if bloodmoon isn't loaded
        if(!SRPConfigSystems.useEvolution && !phaseRules.isEmpty()) return 1; //Ignores all lines that mention phases if phases are disabled
        else if(SRPConfigSystems.useEvolution) //If any phase rule doesnt match, ignore the rule
            for(Map.Entry<Integer, EnumOperation> entry : phaseRules.entrySet())
                if(!entry.getValue().evaluate(phase, entry.getKey()))
                    return 1;
        return multi;
    }
}
