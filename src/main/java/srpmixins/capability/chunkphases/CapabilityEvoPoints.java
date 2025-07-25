package srpmixins.capability.chunkphases;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.ChunkPhaseConfigProvider;
import srpmixins.mixin.customphases.SRPSaveDataAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CapabilityEvoPoints extends SRPSaveData implements ICapabilityEvoPoints {
    private final Chunk chunk;

    private int evoPoints = 0;
    private int evoPointsBuffer = 0;
    private byte evoPhase = 0;
    private long cooldownUntil = 0;

    private Integer currPhaseMin = null;
    private Integer nextPhaseMin = null;
    private boolean isOutsideCurrRange(int points){
        return (currPhaseMin != null && points < currPhaseMin) || (nextPhaseMin != null && points >= nextPhaseMin);
    }

    //buffers in both directions +-, balanced against min possible reduction with carcasses (yep, carcasses use luredValueXCool for whatever reason)
    private static final int evoPointsBufferOverflow = SRPMixinsConfigProvider.getLurePhaseMultiplier((byte) 0) * SRPConfigSystems.luredValueOneCool;
    private static boolean recursionStarted = false;

    public CapabilityEvoPoints(){
        this(null, (byte) -9);
    }

    public CapabilityEvoPoints(Chunk chunk, byte startPhase){
        this.chunk = chunk;
        if(chunk != null)
            createData(chunk.getWorld(), startPhase);
    }

    public void updateNearby(Consumer<ICapabilityEvoPoints> chunkCapFunction){
        if(recursionStarted) return;
        recursionStarted = true;

        int chunkRadius = SRPMixinsConfigHandler.chunkphases.regionUpdateRadius;
        int chunkSpacing = ChunkPhaseConfigProvider.chunkPhasesSpacing;
        for (int i = -chunkRadius; i <= chunkRadius; i++) {
            for (int j = -chunkRadius; j <= chunkRadius; j++) {
                if(i == 0 && j == 0) continue; //Don't update the current chunk again
                Chunk chunkNearby = chunk.getWorld().getChunkProvider().getLoadedChunk(chunk.x + i*chunkSpacing, chunk.z + j*chunkSpacing);
                if(chunkNearby == null) continue; //we don't load unloaded chunks for this
                ICapabilityEvoPoints nearbyCap = chunkNearby.getCapability(CapabilityEvoPointsHandler.CAP_EVOPOINTS, null);
                if (nearbyCap == null) continue;

                chunkCapFunction.accept(nearbyCap);
            }
        }

        recursionStarted = false;
    }

    @Override
    public boolean addEvoPoints(int addedPoints, long currentTime, boolean canChangePhase) {
        if(addedPoints == 0) return false;

        //Chunks with evo phase -2 don't change points
        if (this.evoPhase == -2) return false;

        //Reducing during cooldown allowed, increasing isn't.
        // This will also stop nearby chunks from getting the points
        if (addedPoints > 0 || !SRPMixinsConfigHandler.lures.fixCarcassDuringCooldown)
            if (getCooldownTicks(currentTime) > 0) return false;

        //only buffer for genuine point additions via setTotalKills, not for updateNearby
        boolean bufferDidOverFlow = false;
        if(!recursionStarted) {
            this.evoPointsBuffer += addedPoints;

            //Also overflow if we would change phase of current chunk with it
            boolean wouldChangePhase = isOutsideCurrRange(this.evoPoints + this.evoPointsBuffer);
            if(Math.abs(this.evoPointsBuffer) >= evoPointsBufferOverflow || wouldChangePhase) {
                //Buffer Overflow adds all stored points in buffer to current points
                this.evoPoints += this.evoPointsBuffer;
                addedPoints = this.evoPointsBuffer; //just for later checks
                bufferDidOverFlow = true;
            }
        } else
            this.evoPoints += addedPoints;

        //Don't do limit checks, phase checks or update nearby (!recursionStarted) if points were added to buffer (!bufferDidOverflow)
        // --> if(!(!recursionStarted && !bufferDidOverflow)) same as below
        if(recursionStarted || bufferDidOverFlow){
            //Don't increase further than absolute max
            this.evoPoints = Math.min(this.evoPoints, SRPConfigSystems.phaseTenTotalPoints);
            if (this.evoPhase >= 0) {
                //Don't reduce further than 0 if phase is not -1 or -2
                this.evoPoints = Math.max(this.evoPoints, 0);
                //Don't reduce further than phase min if config is enabled
                if (SRPMixinsConfigHandler.phasepoints.limitPointReduction && !canChangePhase && addedPoints < 0)
                    this.evoPoints = Math.max(this.evoPoints, SRPConfigProvider.getPhaseMinPoints(this.evoPhase));
            }

            //Check if phase got changed. Don't reduce further than phase 0 or increase above phase 10
            if (canChangePhase && !(addedPoints < 0 && this.evoPhase <= 0) && !(addedPoints > 0 && this.evoPhase >= SRPConfigProvider.getMaxPhase())) {
                //checking this before going through the whole list is just for performance
                //Phase can also reduce multiple phases at once, so we can't just -- or ++
                if (isOutsideCurrRange(this.evoPoints)) {
                    byte newPhase = -1;
                    //newPhase is always one lower than the phase belonging to current phasePointMin
                    //so when the comparison fails we're already at the correct phase
                    for (byte phase = 0; phase <= SRPConfigProvider.getMaxPhase(); phase++)
                        if (this.evoPoints >= SRPConfigProvider.getPhaseMinPoints(phase)) newPhase++;
                        else break;
                    if (this.evoPhase != newPhase) //In theory unnecessary
                        setEvoPhase(newPhase);
                }
            }

            //Add points to nearby chunks
            if(bufferDidOverFlow) {
                updateNearby(c -> c.addEvoPoints(this.evoPointsBuffer, currentTime, canChangePhase));
                this.evoPointsBuffer = 0;
            }
        }

        return true;
    }

    @Override
    public void setEvoPhase(byte phase) {
        this.evoPhase = phase;
        currPhaseMin = SRPConfigProvider.getPhaseMinPoints(this.evoPhase);
        nextPhaseMin = SRPConfigProvider.getPhaseMinPoints((byte) (this.evoPhase + 1));
        if(this.evoPhase < 0) currPhaseMin = null;
        if(this.evoPhase == -2) nextPhaseMin = null;
        if(this.evoPhase == SRPConfigProvider.getMaxPhase()) nextPhaseMin = SRPConfigSystems.phaseTenTotalPoints+1;

        // Cooldown, only for the current chunk
        int phaseCooldownSeconds = SRPConfigProvider.getPhaseCooldown(this.evoPhase);
        setCooldown(20 * phaseCooldownSeconds, chunk.getWorld().getWorldTime(), false);

        // Unlock parasites, global, not chunk dependent
        SRPSaveDataAccessor data = instanceAccessor();
        //First call of SRPSaveData.instance needs to create it from worldStorage NBT
        if(data == null) data = ((SRPSaveDataAccessor) SRPSaveData.get(chunk.getWorld()));
        data.invokeCheckForUnlock(this.evoPhase, chunk.getWorld().provider.getDimension(), chunk.getWorld());
    }

    @Override
    public void setCooldown(int cooldown, long currentTime, boolean doUpdateNearby) {
        this.cooldownUntil = currentTime + cooldown;

        //Add cooldown to nearby chunks
        if(doUpdateNearby) updateNearby(c -> c.addCooldownTicks(cooldown, currentTime));
    }

    @Override
    public void addCooldownTicks(int cooldown, long currentTime) {
        if(cooldown <= 0) return;

        //No current timer or old cooldown ran out
        if(getCooldownTicks(currentTime) <= 0)
            this.cooldownUntil = currentTime;

        this.cooldownUntil += cooldown;
    }

    @Override
    public int getCooldownTicks(long currentTime) {
        return (int) (this.cooldownUntil - currentTime);
    }

    //Simulates SRPSaveData.checkPhase behavior
    @Override
    public void setPointsToPhaseStart() {
        this.evoPoints = SRPConfigProvider.getPhaseMinPoints(this.evoPhase);
    }

    //---------------------------------------
    //------- Cap NBT Storage getters -------
    //---------------------------------------

    @Override
    public void setEvoPointsFromNBT(int points) {
        this.evoPoints = points;
    }

    @Override
    public int getEvoPoints() {
        return evoPoints;
    }

    @Override
    public void setEvoPointsBufferFromNBT(int pointsBuffer) {
        this.evoPointsBuffer = pointsBuffer;
    }

    @Override
    public int getEvoPointsBuffer() {
        return evoPointsBuffer;
    }

    @Override
    public void setEvoPhaseFromNBT(byte phase) {
        this.evoPhase = phase;
    }

    @Override
    public byte getEvoPhase() {
        return this.evoPhase;
    }

    @Override
    public void setCooldownUntilFromNBT(long cooldownUntilWorldTime) {
        this.cooldownUntil = cooldownUntilWorldTime;
    }

    @Override
    public long getCooldownUntil() {
        return this.cooldownUntil;
    }

    //------------------------------------
    //------- SRPSaveData Override -------
    //------------------------------------

    //Not an override, because we don't use mapStorage
    private void createData(World world, byte startPhase) {
        int currentDim = world.provider.getDimension();
        //No default -> start at -100 points, -2 phase, 0 cooldown
        int phase = -2;
        int points = -100;

        if(startPhase != -9){
            phase = startPhase;
            if(startPhase >= 0)
                points = SRPConfigProvider.getPhaseMinPoints(startPhase);
        } else {
            List<Integer> dimensionStartSettings = SRPConfigProvider.evolutionStartPerDimension.get(currentDim);
            if (dimensionStartSettings != null) {
                phase = dimensionStartSettings.get(0);
                points = dimensionStartSettings.get(1);
            }
            if (phase == -1 && points > 0) points = -points; //bruh
        }

        setEvolutionPhase(currentDim, (byte) phase, true, world, true);
        this.evoPoints = points;
    }

    @Override
    public boolean setTotalKills(int dimId, int points, boolean isAddingNotSetting, World worldIn, boolean canChangePhase) {
        //Config is described incorrectly, if true parasites CAN'T get points with no players online
        if (SRPConfigSystems.evolutionNoPlayerMultipler && worldIn.playerEntities.isEmpty()) return false;

        if (isAddingNotSetting) {
            //Dimension can't gain points
            if (!getCanGain(dimId) && points > 0) return false;
            //Dimension can't lose points
            if (getCanLoss(dimId) && points < 0) return false;

            return addEvoPoints(points, worldIn.getWorldTime(), canChangePhase);
        }

        //Only used by setphase command (for phase -1 and -2).
        // SRP also goes this route with SRPSaveData.checkKills, but we set directly via setEvoPoints
        this.evoPoints = points;
        return true;
    }

    //Only used by SRP command getphase and by modified bloody clock
    //Technically slightly inaccurate since buffer overflow will update nearby chunks, but it's fine
    @Override
    public int getTotalKills(int id) {
        return this.evoPoints + this.evoPointsBuffer;
    }

    //This whole thing is only called for SRPCommand setphase and will only update current chunk
    @Override
    public boolean setEvolutionPhase(int dimId, byte phase, boolean doRefreshPoints, World worldIn, boolean canChangePhase) {
        //Don't understand, why do we set points back to start of phase?
        // in this chunk implementation this would never run, but we keep it in for compat
        if (!canChangePhase) {
            setPointsToPhaseStart();
            return false;
        }

        setEvoPhase(phase);

        if (doRefreshPoints) setPointsToPhaseStart();

        return true;
    }

    @Override
    public byte getEvolutionPhase(int id) {
        return this.evoPhase;
    }

    //Only used by SRP command setCooldown and BlockEvolutionLure
    @Override
    public void setCooldown(int cooldown, World worldIn, int dimId) {
        setCooldown(cooldown * 20, worldIn.getWorldTime(), true);
    }

    @Override
    public int getCooldown(World worldIn, int dimId) {
        return Math.max(0,getCooldownTicks(worldIn.getWorldTime())) / 20;
    }

    //--------------------------------------------------
    //------- Redirects to SRPSaveData.instance  -------
    //--------------------------------------------------

    private SRPSaveData instance(){
        return ((SRPSaveDataAccessor) this).getInstance();
    }

    private SRPSaveDataAccessor instanceAccessor(){
        return (SRPSaveDataAccessor) instance();
    }

    private void markInstanceDirty(){
        instance().markDirty();
    }

    @Override
    public boolean checkParasiteID(int parasiteId) {
        return getLockedList().contains(parasiteId);
    }

    @Override
    public ArrayList<Integer> getLockedList() {
        return instanceAccessor().getLockedParasites();
    }

    @Override
    public void resetLock() {
        getLockedList().clear();
        getLockedList().addAll(SRPConfigProvider.lockedParasites);
        markInstanceDirty();
    }

    @Override
    public void setGaining(boolean canGain, int dimId) {
        int index = instanceAccessor().getDimEPid().indexOf(dimId);
        if(index != -1) {
            instanceAccessor().getDimEPcanGainPoints().set(index, canGain);
            markInstanceDirty();
        }
    }

    @Override
    public boolean getCanGain(int id) {
        int index = instanceAccessor().getDimEPid().indexOf(id);
        if(index != -1)
            return instanceAccessor().getDimEPcanGainPoints().get(index);
        return false;
    }

    @Override
    public void setLoss(boolean cantLose, int dimId) {
        int index = instanceAccessor().getDimEPid().indexOf(dimId);
        if(index != -1) {
            instanceAccessor().getDimEPcanLossPoints().set(index, cantLose);
            markInstanceDirty();
        }
    }

    @Override
    public boolean getCanLoss(int id) {
        int index = instanceAccessor().getDimEPid().indexOf(id);
        if(index != -1)
            return instanceAccessor().getDimEPcanLossPoints().get(index);
        //Should return true, but we mimic SRP behavior. Should never happen anyway
        return false;
    }

    @Override
    public int getNumberIDDataSpawn(int id) {
        int index = instanceAccessor().getSimRegId().indexOf((byte) id);
        if(index != -1)
            return instanceAccessor().getSimRegIdTimes().get(index);
        return SRPMixinsConfigHandler.coth.fixMinAssimilations ? 0 : -1;
    }

    @Override
    public void addNumberIDDataSpawn(int id) {
        int index = instanceAccessor().getSimRegId().indexOf((byte) id);
        if(index != -1){
            int currentValue = instanceAccessor().getSimRegIdTimes().get(index);
            instanceAccessor().getSimRegIdTimes().set(index, currentValue + 1);
        } else {
            instanceAccessor().getSimRegId().add((byte)id);
            instanceAccessor().getSimRegIdTimes().add(1);
        }
    }
}
