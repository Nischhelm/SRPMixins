package srpmixins.util.customphasemechanics;

import srpmixins.mixin.customphases.SRPSaveDataAccessor;

import java.util.ArrayList;
import java.util.List;

public class SRPSaveDataPlayerLegacyPatch {
    //This patch is only necessary because i accidentally saved one SRPSaveData per dimension instead of a single one at /data = overworld
    //Patched in SRPMixins 2.8.6 and onwards

    private final int dimId;
    private int points;
    private byte phase;
    private int time;
    private boolean canGain;
    private boolean cantLose;
    private List<Integer> lockedParaIds;
    private List<Byte> simParaIds;
    private List<Integer> simCounters;

    private boolean isValid = true;

    public SRPSaveDataPlayerLegacyPatch(int dimId, SRPSaveDataAccessor legacyData){
        this.dimId = dimId;
        int idx = legacyData.getDimEPid().indexOf(dimId);
        if(idx == -1){
            isValid = false;
            return;
        }
        this.points = legacyData.getDimEPtotalKills().get(idx);
        this.phase = legacyData.getDimEPevolution().get(idx);
        this.time = legacyData.getDimEPtimeEvolution().get(idx);
        this.canGain = legacyData.getDimEPcanGainPoints().get(idx);
        this.cantLose = legacyData.getDimEPcanLossPoints().get(idx);
        this.lockedParaIds = legacyData.getLockedParasites();
        this.simParaIds = legacyData.getSimRegId();
        this.simCounters = legacyData.getSimRegIdTimes();
    }

    public void patchWithLegacyData(SRPSaveDataAccessor actualData) {
        if(!this.isValid) return;
        int actualIdx = actualData.getDimEPid().indexOf(this.dimId);
        if(actualIdx == -1){
            actualIdx = actualData.getDimEPid().size(); //index of newest entry
            actualData.getDimEPid().add(this.dimId);
        }

        actualData.getDimEPtotalKills().set(actualIdx, this.points);
        actualData.getDimEPevolution().set(actualIdx, this.phase);
        actualData.getDimEPtimeEvolution().set(actualIdx, this.time);
        actualData.getDimEPcanGainPoints().set(actualIdx, this.canGain);
        actualData.getDimEPcanLossPoints().set(actualIdx, this.cantLose);

        //Only keep the locked paras that are in both lists (if it got unlocked in one dimension, it should be unlocked=gone in the other as well)
        List<Integer> commonLockedParaIds = new ArrayList<>();
        for(int paraId : actualData.getLockedParasites()){
            if(this.lockedParaIds.contains(paraId))
                commonLockedParaIds.add(paraId);
        }
        actualData.getLockedParasites().clear();
        actualData.getLockedParasites().addAll(commonLockedParaIds);

        //Add sim counters from legacy dimension to actual counter
        for(int i = 0; i < this.simParaIds.size(); i++){
            byte paraId = this.simParaIds.get(i);
            int counterInLegacy = this.simCounters.get(i);

            int idxInActual = actualData.getSimRegId().indexOf(paraId);
            if(idxInActual == -1) {
                idxInActual = actualData.getSimRegId().size(); //append
                actualData.getSimRegId().add(paraId);
            }
            int counterInActual = actualData.getSimRegIdTimes().get(idxInActual);
            actualData.getSimRegIdTimes().set(idxInActual, counterInActual + counterInLegacy);
        }
    }
}
