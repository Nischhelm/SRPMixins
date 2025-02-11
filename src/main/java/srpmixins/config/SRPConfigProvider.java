package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.util.math.MathHelper;

import java.util.*;
import java.util.stream.Collectors;

public class SRPConfigProvider {
    public static final List<Integer> phaseCooldowns = Arrays.asList(
            0,
            SRPConfigSystems.phaseDelayTicksOne,
            SRPConfigSystems.phaseDelayTicksTwo,
            SRPConfigSystems.phaseDelayTicksThree,
            SRPConfigSystems.phaseDelayTicksFour,
            SRPConfigSystems.phaseDelayTicksFive,
            SRPConfigSystems.phaseDelayTicksSix,
            SRPConfigSystems.phaseDelayTicksSeven,
            SRPConfigSystems.phaseDelayTicksEight,
            SRPConfigSystems.phaseDelayTicksNine,
            SRPConfigSystems.phaseDelayTicksTen
    );

    public static final List<Integer> phasePointThresholds = Arrays.asList(
            0,
            SRPConfigSystems.phaseKillsOne,
            SRPConfigSystems.phaseKillsTwo,
            SRPConfigSystems.phaseKillsThree,
            SRPConfigSystems.phaseKillsFour,
            SRPConfigSystems.phaseKillsFive,
            SRPConfigSystems.phaseKillsSix,
            SRPConfigSystems.phaseKillsSeven,
            SRPConfigSystems.phaseKillsEight,
            SRPConfigSystems.phaseKillsNine,
            SRPConfigSystems.phaseKillsTen
    );

    public static int getPhaseMinPoints(byte phase){
        return phasePointThresholds.get(MathHelper.clamp(phase, 0, 10));
    }

    public static final List<Double> reinForcementChancePerPhase = Arrays.asList(
            0.0,
            SRPConfigSystems.reinforcementSystemChanceOne,
            SRPConfigSystems.reinforcementSystemChanceTwo,
            SRPConfigSystems.reinforcementSystemChanceThree,
            SRPConfigSystems.reinforcementSystemChanceFour,
            SRPConfigSystems.reinforcementSystemChanceFive,
            SRPConfigSystems.reinforcementSystemChanceSix,
            SRPConfigSystems.reinforcementSystemChanceSeven,
            SRPConfigSystems.reinforcementSystemChanceEight,
            SRPConfigSystems.reinforcementSystemChanceNine,
            SRPConfigSystems.reinforcementSystemChanceTen
    );

    public static final List<Integer> dimensionCanGainPointsBlacklist = Arrays.stream(SRPConfigSystems.evolutionDimGain).boxed().collect(Collectors.toList());
    public static final List<Integer> dimensionCantLosePointsBlacklist = Arrays.stream(SRPConfigSystems.evolutionDimLoss).boxed().collect(Collectors.toList());

    //dimensionId, [phase, points]
    public static final Map<Integer, List<Integer>> evolutionStartPerDimension = new HashMap<>();
    public static final List<Integer> lockedParasites = new ArrayList<>();

    public static void init(){
        for(String s : SRPConfigSystems.evolutionParasiteLock) {
            if (s != null) {
                String[] split = s.split(";");
                int id = Integer.parseInt(split[2]);
                lockedParasites.add(id);
            }
        }

        for (String s : SRPConfigSystems.evolutionDimStart) {
            String[] split = s.split(";");
            int dim = Integer.parseInt(split[0]);
            int phase = Integer.parseInt(split[1]);
            int points = Integer.parseInt(split[2]);
            evolutionStartPerDimension.put(dim, Arrays.asList(phase, points));
        }
    }
}
