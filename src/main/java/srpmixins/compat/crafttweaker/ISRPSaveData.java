package srpmixins.compat.crafttweaker;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import srpmixins.SRPMixins;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenRegister
@ZenClass(SRPMixins.MODID + ".SRPSaveData")
@SuppressWarnings("unused")
public class ISRPSaveData {
    private static World getOverworld(){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(0);
    }
    
    SRPSaveData internal;

    public ISRPSaveData(SRPSaveData internal){
        this.internal = internal;
    }

    @ZenMethod("getForPlayer")
    @ZenDoc("Returns a phase/point/cooldown container for the provided IPlayer (only useful if player phases is enabled)")
    public static ISRPSaveData getForPlayer(IPlayer iPlayer){
        EntityPlayer player = (EntityPlayer) iPlayer.getInternal();
        return new ISRPSaveData(SRPSaveDataInterface.get(player.world, player, null));
    }

    @ZenMethod("getForBlockPos")
    @ZenDoc("Returns a phase/point/cooldown container for the chunk at the provided IBlockPos or nearest player to the provided IBlockPos (only useful if chunk phases or player phases are enabled)")
    public static ISRPSaveData getForBlockPos(IBlockPos pos){
        return new ISRPSaveData(SRPSaveDataInterface.get(getOverworld(), null, (BlockPos) pos.getInternal()));
    }

    @ZenMethod("get")
    @ZenDoc("Returns a global phase/point/cooldown container (only useful if player phases and chunk phases are disabled)")
    public static ISRPSaveData getGlobal(){
        return new ISRPSaveData(SRPSaveData.get(getOverworld()));
    }

    // GETTERS

    @ZenMethod("getPhase")
    @ZenDoc("Returns the current evolution phase in the provided dimension")
    public byte getPhase(int dim){
        return this.internal.getEvolutionPhase(dim);
    }

    @ZenMethod("getPoints")
    @ZenDoc("Returns the current evolution points in the provided dimension")
    public int getPoints(int dim){
        return this.internal.getTotalKills(dim);
    }

    @ZenMethod("getCooldown")
    @ZenDoc("Returns the current cooldown (in ticks) in the provided dimension")
    public int getCooldown(int dim){
        return this.internal.getCooldown(getOverworld(), dim);
    }

    @ZenMethod("getCanLose")
    @ZenDoc("Returns whether the evolution points in the provided dimension can be reduced (via carcasses for example). Note: Different than base SRP this is not inverted in meaning, so true means can reduce, false means can't reduce.")
    public boolean getCanLose(int dim){
        return !this.internal.getCanLoss(dim);
    }

    @ZenMethod("getCanGain")
    @ZenDoc("Returns whether the evolution points in the provided dimension can be increased.")
    public boolean getCanGain(int dim){
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
    @ZenDoc("Sets the evolution phase in the provided dimension (params: phase, dimensionId)")
    public void setPhase(byte phase, int dim){
        this.internal.setEvolutionPhase(dim, phase, true, getOverworld(), true);
    }

    @ZenMethod("setPoints")
    @ZenDoc("Sets the evolution points in the provided dimension (params: points, dimensionId)")
    public void setPoints(int points, int dim){
        this.internal.setTotalKills(points, dim, false, getOverworld(), true);
    }

    @ZenMethod("addPoints")
    @ZenDoc("Adds to the current evolution points in the provided dimension (params: pointsToAdd, dimensionId)")
    public void addPoints(int points, int dim){
        this.internal.setTotalKills(points, dim, true, getOverworld(), true);
    }

    @ZenMethod("setCooldown")
    @ZenDoc("Sets the cooldown in the provided dimension (params: pointsToAdd, dimensionId)")
    public void setCooldown(int cooldown, int dim){
        this.internal.setCooldown(cooldown, getOverworld(), dim);
    }

    @ZenMethod("addCooldown")
    @ZenDoc("Adds to the cooldown in the provided dimension (params: cooldownToAdd, dimensionId)")
    public void addCooldown(int cooldown, int dim){
        int currCooldown = this.internal.getCooldown(getOverworld(), dim);
        this.internal.setCooldown(currCooldown + cooldown, getOverworld(), dim);
    }

    @ZenMethod("setCanLose")
    @ZenDoc("Sets whether the provided dimension can have its evolution points reduced (params: newCanLose, dimensionId). Note: Different than base SRP this is not inverted in meaning, so true means can reduce, false means can't reduce.")
    public void setCanLose(boolean newValue, int dim){
        this.internal.setLoss(!newValue, dim);
    }

    @ZenMethod("setCanGain")
    @ZenDoc("Sets whether the provided dimension can have its evolution points increased (params: newCanGain, dimensionId).")
    public void setCanGain(boolean newValue, int dim){
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
}
