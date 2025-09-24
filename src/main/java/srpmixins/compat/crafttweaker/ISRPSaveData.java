package srpmixins.compat.crafttweaker;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import srpmixins.SRPMixins;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

import java.util.List;

@ZenRegister
@ZenClass(SRPMixins.MODID + ".SRPSaveData")
@SuppressWarnings("unused")
public class ISRPSaveData {
    private static World getOverworld(){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(0);
    }
    
    SRPSaveData internal;
    int dim;

    public ISRPSaveData(SRPSaveData internal, int dimension){
        this.internal = internal;
        this.dim = dimension;
    }

    @ZenMethod("getForPlayer")
    @ZenDoc("Returns a phase/point/cooldown container for the provided IPlayer (only useful if player phases is enabled)")
    public static ISRPSaveData getForPlayer(IPlayer iPlayer){
        EntityPlayer player = (EntityPlayer) iPlayer.getInternal();
        return new ISRPSaveData(SRPSaveDataInterface.get(player.world, player, null), player.world.provider.getDimension());
    }

    @ZenMethod("getForBlockPos")
    @ZenDoc("Returns a phase/point/cooldown container for the chunk in the provided world at the provided IBlockPos or nearest player to the provided IBlockPos (only useful if chunk phases or player phases are enabled)")
    public static ISRPSaveData getForBlockPos(IWorld iworld, IBlockPos pos){
        World world = (World) iworld.getInternal();
        return new ISRPSaveData(SRPSaveDataInterface.get(world, null, (BlockPos) pos.getInternal()), world.provider.getDimension());
    }

    @ZenMethod("get")
    @ZenDoc("Returns a global phase/point/cooldown container for the provided world (only useful if player phases and chunk phases are disabled)")
    public static ISRPSaveData getGlobal(IWorld iworld){
        World world = (World) iworld.getInternal();
        return new ISRPSaveData(SRPSaveData.get(world), world.provider.getDimension());
    }

    // GETTERS

    @ZenMethod("getPhase")
    @ZenGetter("phase")
    @ZenDoc("Returns the current evolution phase")
    public byte getPhase(){
        return this.internal.getEvolutionPhase(dim);
    }

    @ZenMethod("getPoints")
    @ZenGetter("points")
    @ZenDoc("Returns the current evolution points")
    public int getPoints(){
        return this.internal.getTotalKills(dim);
    }

    @ZenMethod("getCooldown")
    @ZenGetter("cooldown")
    @ZenDoc("Returns the current cooldown (in ticks)")
    public int getCooldown(){
        return this.internal.getCooldown(getOverworld(), dim);
    }

    @ZenMethod("getCanLose")
    @ZenGetter("canLose")
    @ZenDoc("Returns whether the evolution points can be reduced (via carcasses for example). Note: Different than base SRP this is not inverted in meaning, so true means can reduce, false means can't reduce.")
    public boolean getCanLose(){
        return !this.internal.getCanLoss(dim);
    }

    @ZenMethod("getCanGain")
    @ZenGetter("canGain")
    @ZenDoc("Returns whether the evolution points can be increased.")
    public boolean getCanGain(){
        return this.internal.getCanGain(dim);
    }

    @ZenMethod("getAssimilationCount")
    @ZenDoc("Returns how often an assimilated/feral/hijacked parasite with the given parasite name (example: sim_human) has been created via assimilation, which is needed for some parasites to naturally spawn. Thresholds are defined in configs.")
    public int getAssimilationCount(String paraName){
        return this.internal.getNumberIDDataSpawn(SRPMobConfigProvider.mobNameToParaIdMap.get(paraName));
    }

    @ZenMethod("isEvoLocked")
    @ZenDoc("Returns whether a parasite with the given parasite name (example: sim_dragone) is evolution locked. If it is locked, it cannot spawn naturally until it is unlocked. See SRParasitesSystems.cfg/\"Evolution Parasite Lock List\" for the base system. Parasites can also be locked/unlocked just using the provided methods here.")
    public boolean isEvoLocked(String paraName){
        return this.internal.checkParasiteID(SRPMobConfigProvider.mobNameToParaIdMap.get(paraName));
    }

    // SETTERS

    @ZenMethod("setPhase")
    @ZenSetter("phase")
    @ZenDoc("Sets the evolution phase (params: phase)")
    public void setPhase(byte phase){
        this.internal.setEvolutionPhase(dim, phase, true, getOverworld(), true);
    }

    @ZenMethod("setPoints")
    @ZenSetter("points")
    @ZenDoc("Sets the evolution points (params: points)")
    public void setPoints(int points){
        this.internal.setTotalKills(dim, points, false, getOverworld(), true);
    }

    @ZenMethod("addPoints")
    @ZenDoc("Adds to the current evolution points (params: pointsToAdd)")
    public void addPoints(int points){
        this.internal.setTotalKills(dim, points, true, getOverworld(), true);
    }

    @ZenMethod("setCooldown")
    @ZenSetter("cooldown")
    @ZenDoc("Sets the cooldown in seconds (params: pointsToAdd)")
    public void setCooldown(int cooldown){
        this.internal.setCooldown(cooldown, getOverworld(), dim);
    }

    @ZenMethod("addCooldown")
    @ZenDoc("Adds seconds to the cooldown (params: cooldownToAdd)")
    public void addCooldown(int cooldown){
        int currCooldown = this.internal.getCooldown(getOverworld(), dim);
        this.internal.setCooldown(currCooldown + cooldown, getOverworld(), dim);
    }

    @ZenMethod("setCanLose")
    @ZenSetter("canLose")
    @ZenDoc("Sets whether the SRPSaveData of the provided dimension can have its evolution points reduced (params: newCanLose). Note: Different than base SRP this is not inverted in meaning, so true means can reduce, false means can't reduce.")
    public void setCanLose(boolean newValue){
        this.internal.setLoss(!newValue, dim);
    }

    @ZenMethod("setCanGain")
    @ZenSetter("canGain")
    @ZenDoc("Sets whether the SRPSaveData of the provided dimension can have its evolution points increased (params: newCanGain).")
    public void setCanGain(boolean newValue){
        this.internal.setGaining(newValue, dim);
    }

    @ZenMethod("increaseAssimilationCount")
    @ZenDoc("Increases the assimilation count of the provided assimilated/feral/hijacked parasite name (example: sim_human) by one.")
    public void increaseAssimilationCount(String paraName){
        this.internal.addNumberIDDataSpawn(SRPMobConfigProvider.mobNameToParaIdMap.get(paraName));
    }

    @ZenMethod("unlockEvoLockForParasite")
    @ZenDoc("Unlocks the evolution lock of the parasite with the provided parasite name (example: sim_dragone).")
    public void unlockEvoLockForParasite(String paraName){
        this.internal.unlockParasite(SRPMobConfigProvider.mobNameToParaIdMap.get(paraName));
    }

    @ZenMethod("lockParasite")
    @ZenDoc("Puts the parasite with the provided parasite name (example: sim_dragone) on the evolution lock list, preventing it from naturally spawning. Note: as this is not using the SRP config defined mechanic, you will have to manually unlock the parasite under your own conditions in your CT script.")
    public void lockParasite(String paraName){
        List<Integer> lockedList = this.internal.getLockedList();
        int paraId = SRPMobConfigProvider.mobNameToParaIdMap.get(paraName);
        if(!lockedList.contains(paraId))
            lockedList.add(paraId);
    }

    @ZenMethod("resetEvoLock")
    @ZenDoc("Resets the evolution lock list to the SRP config defined list (see SRParasitesSystems.cfg/\"Evolution Parasite Lock List\".")
    public void resetEvoLock(){
        this.internal.resetLock();
    }

    @ZenMethod("getPointThreshold")
    @ZenDoc("Returns how many points are required for the given phase")
    public static int getPointThreshold(byte phase){
        return SRPConfigProvider.getPhaseMinPoints(phase);
    }
}
