package srpmixins.config.providers;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MorePhasesConfigProvider {

    public static final List<List<Float>> nexusGrowStunChance = new ArrayList<>();

    public static void reset(){
        nexusGrowStunChance.clear();
        postInit();
    }

    public static void postInit(){
        for(String s : SRPMixinsConfigHandler.morephases.nexusGrowPenalty){
            String[] split = s.split(";");
            if(split.length < 3){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Nexus Grow Stunned entry, too few arguments (expected 3): {}", s);
                continue;
            }
            try {
                float penaltyStageI = Float.parseFloat(split[0].trim());
                float penaltyStageII = Float.parseFloat(split[1].trim());
                float penaltyStageIII = Float.parseFloat(split[2].trim());
                nexusGrowStunChance.add(Arrays.asList(penaltyStageI, penaltyStageII, penaltyStageIII));
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Nexus Grow Stun Chance entry. Expected pattern: stunChanceStage1; stunChanceStage2; stunChanceStage3. Provided was {}", s);
            }
        }
    }

    //This is only called once if "More Phases" is enabled but the config isn't filled with current SRP values yet
    public static void initMorePhasesConfig() {
        int[] phaseKills = {0, SRPConfigSystems.phaseKillsOne, SRPConfigSystems.phaseKillsTwo, SRPConfigSystems.phaseKillsThree, SRPConfigSystems.phaseKillsFour, SRPConfigSystems.phaseKillsFive, SRPConfigSystems.phaseKillsSix, SRPConfigSystems.phaseKillsSeven, SRPConfigSystems.phaseKillsEight, SRPConfigSystems.phaseKillsNine, SRPConfigSystems.phaseKillsTen};
        double[] phaseKillCountPlus = {0.0F, SRPConfigSystems.phaseKillCountPlusOne, SRPConfigSystems.phaseKillCountPlusTwo, SRPConfigSystems.phaseKillCountPlusThree, SRPConfigSystems.phaseKillCountPlusFour, SRPConfigSystems.phaseKillCountPlusFive, SRPConfigSystems.phaseKillCountPlusSix, SRPConfigSystems.phaseKillCountPlusSeven, SRPConfigSystems.phaseKillCountPlusEight, SRPConfigSystems.phaseKillCountPlusNine, SRPConfigSystems.phaseKillCountPlusTen};
        String[] phaseWarning = {"", SRPConfigSystems.phaseWarningOne, SRPConfigSystems.phaseWarningTwo, SRPConfigSystems.phaseWarningThree, SRPConfigSystems.phaseWarningFour, SRPConfigSystems.phaseWarningFive, SRPConfigSystems.phaseWarningSix, SRPConfigSystems.phaseWarningSeven, SRPConfigSystems.phaseWarningEight, SRPConfigSystems.phaseWarningNine, SRPConfigSystems.phaseWarningTen};
        int[] phaseDelayTicks = {0, SRPConfigSystems.phaseDelayTicksOne, SRPConfigSystems.phaseDelayTicksTwo, SRPConfigSystems.phaseDelayTicksThree, SRPConfigSystems.phaseDelayTicksFour, SRPConfigSystems.phaseDelayTicksFive, SRPConfigSystems.phaseDelayTicksSix, SRPConfigSystems.phaseDelayTicksSeven, SRPConfigSystems.phaseDelayTicksEight, SRPConfigSystems.phaseDelayTicksNine, SRPConfigSystems.phaseDelayTicksTen};
        int[] sleepPenalty = {SRPConfigSystems.sleepPenaltyZero, SRPConfigSystems.sleepPenaltyOne, SRPConfigSystems.sleepPenaltyTwo, SRPConfigSystems.sleepPenaltyThree, SRPConfigSystems.sleepPenaltyFour, SRPConfigSystems.sleepPenaltyFive, SRPConfigSystems.sleepPenaltySix, SRPConfigSystems.sleepPenaltySeven, SRPConfigSystems.sleepPenaltyEight, SRPConfigSystems.sleepPenaltyNine, SRPConfigSystems.sleepPenaltyTen};

        int[] phaseMinParasiteID = {SRPConfigSystems.phaseCancelParasiteIDZero, SRPConfigSystems.phaseCancelParasiteIDOne, SRPConfigSystems.phaseCancelParasiteIDTwo, SRPConfigSystems.phaseCancelParasiteIDThree, SRPConfigSystems.phaseCancelParasiteIDFour, SRPConfigSystems.phaseCancelParasiteIDFive, SRPConfigSystems.phaseCancelParasiteIDSix, SRPConfigSystems.phaseCancelParasiteIDSeven, SRPConfigSystems.phaseCancelParasiteIDEight, SRPConfigSystems.phaseCancelParasiteIDNine, SRPConfigSystems.phaseCancelParasiteIDTen};
        int[] phaseMaxParasiteID = {SRPConfigSystems.phaseMaxParasiteIDZero, SRPConfigSystems.phaseMaxParasiteIDOne, SRPConfigSystems.phaseMaxParasiteIDTwo, SRPConfigSystems.phaseMaxParasiteIDThree, SRPConfigSystems.phaseMaxParasiteIDFour, SRPConfigSystems.phaseMaxParasiteIDFive, SRPConfigSystems.phaseMaxParasiteIDSix, SRPConfigSystems.phaseMaxParasiteIDSeven, SRPConfigSystems.phaseMaxParasiteIDEight, SRPConfigSystems.phaseMaxParasiteIDNine, SRPConfigSystems.phaseMaxParasiteIDTen};

        List<List<String>> phaseSpawnListOriginal = Arrays.asList(Arrays.asList(SRPConfigSystems.phaseSpawnEntryZero), Arrays.asList(SRPConfigSystems.phaseSpawnEntryOne), Arrays.asList(SRPConfigSystems.phaseSpawnEntryTwo), Arrays.asList(SRPConfigSystems.phaseSpawnEntryThree), Arrays.asList(SRPConfigSystems.phaseSpawnEntryFour), Arrays.asList(SRPConfigSystems.phaseSpawnEntryFive), Arrays.asList(SRPConfigSystems.phaseSpawnEntrySix), Arrays.asList(SRPConfigSystems.phaseSpawnEntrySeven), Arrays.asList(SRPConfigSystems.phaseSpawnEntryEight), Arrays.asList(SRPConfigSystems.phaseSpawnEntryNine), Arrays.asList(SRPConfigSystems.phaseSpawnEntryTen));
        List<String> newSpawnList = new ArrayList<>();
        List<String> spawnEntriesAlreadyProcessed = new ArrayList<>();

        for(List<String> oldListToProcess : phaseSpawnListOriginal){
            for(String spawnEntry : oldListToProcess) {
                if(spawnEntriesAlreadyProcessed.contains(spawnEntry)) continue;
                spawnEntriesAlreadyProcessed.add(spawnEntry);
                newSpawnList.add("[" + getPhaseListsContainingEntryAsString(spawnEntry, phaseSpawnListOriginal) + "]; " + spawnEntry);
            }
        }
        String[] phaseSpawnList = newSpawnList.toArray(new String[0]);

        double[] reinforcementSystemChance = {0.0, SRPConfigSystems.reinforcementSystemChanceOne, SRPConfigSystems.reinforcementSystemChanceTwo, SRPConfigSystems.reinforcementSystemChanceThree, SRPConfigSystems.reinforcementSystemChanceFour, SRPConfigSystems.reinforcementSystemChanceFive, SRPConfigSystems.reinforcementSystemChanceSix, SRPConfigSystems.reinforcementSystemChanceSeven, SRPConfigSystems.reinforcementSystemChanceEight, SRPConfigSystems.reinforcementSystemChanceNine, SRPConfigSystems.reinforcementSystemChanceTen};
        int[] phaseResidue = {0, SRPConfigSystems.phaseResidueOne, SRPConfigSystems.phaseResidueTwo, SRPConfigSystems.phaseResidueThree, SRPConfigSystems.phaseResidueFour, SRPConfigSystems.phaseResidueFive, SRPConfigSystems.phaseResidueSix, SRPConfigSystems.phaseResidueSeven, SRPConfigSystems.phaseResidueEight, SRPConfigSystems.phaseResidueNine, SRPConfigSystems.phaseResidueTen};
        String[] nexusGrowPenalty = {
                "0; 0; 0",
                SRPConfigSystems.beckonStageIGrowPenaltyOne + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyOne + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyOne,
                SRPConfigSystems.beckonStageIGrowPenaltyTwo + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyTwo + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyTwo,
                SRPConfigSystems.beckonStageIGrowPenaltyThree + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyThree + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyThree,
                SRPConfigSystems.beckonStageIGrowPenaltyFour + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyFour + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyFour,
                SRPConfigSystems.beckonStageIGrowPenaltyFive + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyFive + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyFive,
                SRPConfigSystems.beckonStageIGrowPenaltySix + "; " +SRPConfigSystems.beckonStageIIGrowPenaltySix + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltySix,
                SRPConfigSystems.beckonStageIGrowPenaltySeven + "; " +SRPConfigSystems.beckonStageIIGrowPenaltySeven + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltySeven,
                SRPConfigSystems.beckonStageIGrowPenaltyEight + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyEight + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyEight,
                SRPConfigSystems.beckonStageIGrowPenaltyNine + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyNine + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyNine,
                SRPConfigSystems.beckonStageIGrowPenaltyTen + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyTen + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyTen
        };

        int[] phaseScentBonus = {SRPConfigSystems.phaseScentBonusZero,  SRPConfigSystems.phaseScentBonusOne,  SRPConfigSystems.phaseScentBonusTwo,  SRPConfigSystems.phaseScentBonusThree,  SRPConfigSystems.phaseScentBonusFour,  SRPConfigSystems.phaseScentBonusFive,  SRPConfigSystems.phaseScentBonusSix,  SRPConfigSystems.phaseScentBonusSeven,  SRPConfigSystems.phaseScentBonusEight,  SRPConfigSystems.phaseScentBonusNine,  SRPConfigSystems.phaseScentBonusTen};
        int[] phaseScentReaction = {SRPConfigSystems.phaseScentReactionZero,  SRPConfigSystems.phaseScentReactionOne,  SRPConfigSystems.phaseScentReactionTwo,  SRPConfigSystems.phaseScentReactionThree,  SRPConfigSystems.phaseScentReactionFour,  SRPConfigSystems.phaseScentReactionFive,  SRPConfigSystems.phaseScentReactionSix,  SRPConfigSystems.phaseScentReactionSeven,  SRPConfigSystems.phaseScentReactionEight,  SRPConfigSystems.phaseScentReactionNine,  SRPConfigSystems.phaseScentReactionTen};

        double[] mobSpawningCOTHChance = {0.0, SRPConfigSystems.mobSpawningCOTHChanceOne, SRPConfigSystems.mobSpawningCOTHChanceTwo, SRPConfigSystems.mobSpawningCOTHChanceThree, SRPConfigSystems.mobSpawningCOTHChanceFour, SRPConfigSystems.mobSpawningCOTHChanceFive, SRPConfigSystems.mobSpawningCOTHChanceSix, SRPConfigSystems.mobSpawningCOTHChanceSeven, SRPConfigSystems.mobSpawningCOTHChanceEight, SRPConfigSystems.mobSpawningCOTHChanceNine, SRPConfigSystems.mobSpawningCOTHChanceTen};
        double[] cropGrowStunned = {0.0, SRPConfigSystems.cropGrowStunnedOne, SRPConfigSystems.cropGrowStunnedTwo, SRPConfigSystems.cropGrowStunnedThree, SRPConfigSystems.cropGrowStunnedFour, SRPConfigSystems.cropGrowStunnedFive, SRPConfigSystems.cropGrowStunnedSix, SRPConfigSystems.cropGrowStunnedSeven, SRPConfigSystems.cropGrowStunnedEight, SRPConfigSystems.cropGrowStunnedNine, SRPConfigSystems.cropGrowStunnedTen};

        SRPMixins.CONFIG.get("general.More Phases", "Phase Points", SRPMixinsConfigHandler.morephases.phaseKills).set(phaseKills);
        SRPMixins.CONFIG.get("general.More Phases", "Phase Killcount Plus", SRPMixinsConfigHandler.morephases.phaseKillCountPlus).set(phaseKillCountPlus);
        SRPMixins.CONFIG.get("general.More Phases", "Phase Warning Message", SRPMixinsConfigHandler.morephases.phaseWarning).set(phaseWarning);
        SRPMixins.CONFIG.get("general.More Phases", "Phase Delay", SRPMixinsConfigHandler.morephases.phaseDelayTicks).set(phaseDelayTicks);
        SRPMixins.CONFIG.get("general.More Phases", "Sleep Penalty", SRPMixinsConfigHandler.morephases.sleepPenalty).set(sleepPenalty);
        SRPMixins.CONFIG.get("general.More Phases", "Spawning - Minimum Parasite Type ID", SRPMixinsConfigHandler.morephases.phaseMinParasiteID).set(phaseMinParasiteID);
        SRPMixins.CONFIG.get("general.More Phases", "Spawning - Maximum Parasite Type ID", SRPMixinsConfigHandler.morephases.phaseMaxParasiteID).set(phaseMaxParasiteID);
        SRPMixins.CONFIG.get("general.More Phases", "Spawning - Phase Spawn List", SRPMixinsConfigHandler.morephases.phaseSpawnList).set(phaseSpawnList);
        SRPMixins.CONFIG.get("general.More Phases", "Reinforcement System Chance", SRPMixinsConfigHandler.morephases.reinforcementSystemChance).set(reinforcementSystemChance);
        SRPMixins.CONFIG.get("general.More Phases", "Nexus Grow Stun Chance", SRPMixinsConfigHandler.morephases.nexusGrowPenalty).set(nexusGrowPenalty);
        SRPMixins.CONFIG.get("general.More Phases", "Odds Residue Spawns Beckon", SRPMixinsConfigHandler.morephases.phaseResidue).set(phaseResidue);
        SRPMixins.CONFIG.get("general.More Phases", "Scent Death Bonus", SRPMixinsConfigHandler.morephases.phaseScentBonus).set(phaseScentBonus);
        SRPMixins.CONFIG.get("general.More Phases", "Scent Reaction Bonus", SRPMixinsConfigHandler.morephases.phaseScentReaction).set(phaseScentReaction);
        SRPMixins.CONFIG.get("general.More Phases", "Mobs Spawn With COTH Chance", SRPMixinsConfigHandler.morephases.mobSpawningCOTHChance).set(mobSpawningCOTHChance);
        SRPMixins.CONFIG.get("general.More Phases", "Crop Grow Stunned", SRPMixinsConfigHandler.morephases.cropGrowStunned).set(cropGrowStunned);

        SRPMixinsConfigHandler.morephases.phaseKills = phaseKills;
        SRPMixinsConfigHandler.morephases.phaseKillCountPlus = phaseKillCountPlus;
        SRPMixinsConfigHandler.morephases.phaseWarning = phaseWarning;
        SRPMixinsConfigHandler.morephases.phaseDelayTicks = phaseDelayTicks;
        SRPMixinsConfigHandler.morephases.sleepPenalty = sleepPenalty;
        SRPMixinsConfigHandler.morephases.phaseMinParasiteID = phaseMinParasiteID;
        SRPMixinsConfigHandler.morephases.phaseMaxParasiteID = phaseMaxParasiteID;
        SRPMixinsConfigHandler.morephases.phaseSpawnList = phaseSpawnList;
        SRPMixinsConfigHandler.morephases.reinforcementSystemChance = reinforcementSystemChance;
        SRPMixinsConfigHandler.morephases.nexusGrowPenalty = nexusGrowPenalty;
        SRPMixinsConfigHandler.morephases.phaseResidue = phaseResidue;
        SRPMixinsConfigHandler.morephases.phaseScentBonus = phaseScentBonus;
        SRPMixinsConfigHandler.morephases.phaseScentReaction = phaseScentReaction;
        SRPMixinsConfigHandler.morephases.mobSpawningCOTHChance = mobSpawningCOTHChance;
        SRPMixinsConfigHandler.morephases.cropGrowStunned = cropGrowStunned;

        SRPMixins.CONFIG.save();
    }

    public static String getPhaseListsContainingEntryAsString(String spawnEntry, List<List<String>> phaseSpawnListOriginal) {
        List<Integer> phases = new ArrayList<>();
        String phasesToSpawnIn = "";
        for(int i = 0; i <= phaseSpawnListOriginal.size(); i++) {
            //we go one index further to always clear the list at the end
            if(i < phaseSpawnListOriginal.size() && phaseSpawnListOriginal.get(i).contains(spawnEntry)) {
                phases.add(i);
            }
            //Should trigger on the first spawn list that doesn't contain the current entry
            else if(!phases.isEmpty()){
                //startPhase - endPhase
                if(phases.size() >= 2) phasesToSpawnIn += phases.get(0) + " - " + phases.get(phases.size()-1) + ", ";
                    //single phases
                else phasesToSpawnIn += phases.get(0) + ", ";
                phases.clear();
            }
        }
        return phasesToSpawnIn.substring(0, phasesToSpawnIn.length()-2); //remove last ", "
    }

    public static List<Byte> parsePhaseList(String phasePart) {
        //Pattern: [0 - 2; 4; 10; ...]
        String phasesToSpawnIn = phasePart.trim().replace("[","").replace("]","");
        String[] split = phasesToSpawnIn.split(",");
        List<Byte> spawnPhases = new ArrayList<>();

        try {
            for (String s : split) {
                //Pattern: min - max
                if (s.contains("-")) {
                    String[] split2 = s.split("-");
                    if (split2.length < 2) {
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse Phase part of a Spawn List entry, expected minPhase - maxPhase, provided was: {}", s);
                        continue;
                    }
                    byte minPhase = Byte.parseByte(split2[0].trim());
                    byte maxPhase = Byte.parseByte(split2[1].trim());
                    for (byte i = minPhase; i <= maxPhase; i++)
                        spawnPhases.add(i);
                    //Pattern: specificPhase
                } else
                    spawnPhases.add(Byte.parseByte(s.trim()));
            }
        } catch (Exception e) {
            SRPMixins.LOGGER.warn("SRPMixins unable to parse Phase part of a Spawn List entry, expected pattern [0 - 2; 4; 10; ...]. Provided was: {}", phasePart);
            e.printStackTrace(System.out);
        }

        return spawnPhases;
    }
}
