package srpmixins.rules;

import srpmixins.config.SRPMixinsConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinMaxDayPerPhaseRule {
    private static final List<MinMaxDayPerPhaseRule> allRules = new ArrayList<>();
    public static int getTotalMin(int dimId, byte phase){
        int min = Integer.MAX_VALUE;
        for(MinMaxDayPerPhaseRule rule : allRules) min = Math.min(min, rule.getMin(dimId, phase));
        return min;
    }
    public static int getTotalMax(int dimId, byte phase){
        int max = 0;
        for(MinMaxDayPerPhaseRule rule : allRules) max = Math.max(max, rule.getMax(dimId, phase));
        return max;
    }
    public static void reset(){
        allRules.clear();
        init();
    }
    public static void init(){
        for(String s : SRPMixinsConfigHandler.phasepoints.minMaxDaysPerPhase)
            allRules.add(new MinMaxDayPerPhaseRule(s));
    }

    private Integer dimRule = null;
    private final Map<Integer, EnumOperation> phaseRules = new HashMap<>();
    private int min = Integer.MAX_VALUE, max = 0;

    public MinMaxDayPerPhaseRule(String rule){
        //Config is nice to write with [..] [..] but for parsing it's better to just have rule1, rule2, ... , min, max
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
            else if(s.contains("min")) min = Integer.parseInt(s.replaceFirst("min *=","").trim());
            else if(s.contains("max")) max = Integer.parseInt(s.replaceFirst("max *=","").trim());

            if(min != Integer.MAX_VALUE && max != 0 && min > max) max = min; //If min > max, set them equal
        }
    }

    public int getMin(int dimId, byte phase){
        if(dimRule != null && dimId != dimRule) return Integer.MAX_VALUE;
        for(Map.Entry<Integer, EnumOperation> entry : phaseRules.entrySet())
            if(!entry.getValue().evaluate(phase, entry.getKey()))
                return Integer.MAX_VALUE;
        return min;
    }

    public int getMax(int dimId, byte phase){
        if(dimRule != null && dimId != dimRule) return 0;
        for(Map.Entry<Integer, EnumOperation> entry : phaseRules.entrySet())
            if(!entry.getValue().evaluate(phase, entry.getKey()))
                return 0;
        return max;
    }
}
