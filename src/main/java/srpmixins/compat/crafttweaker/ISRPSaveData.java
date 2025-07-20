package srpmixins.compat.crafttweaker;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import srpmixins.SRPMixins;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(SRPMixins.MODID + "SRPSaveData")
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
    public static ISRPSaveData getForPlayer(IPlayer iPlayer){
        EntityPlayer player = (EntityPlayer) iPlayer.getInternal();
        return new ISRPSaveData(SRPSaveDataInterface.get(player.world, player, null));
    }

    @ZenMethod("getForBlockPos")
    public static ISRPSaveData getForBlockPos(IBlockPos pos){
        return new ISRPSaveData(SRPSaveDataInterface.get(getOverworld(), null, (BlockPos) pos.getInternal()));
    }

    @ZenMethod("get")
    public static ISRPSaveData getGlobal(){
        return new ISRPSaveData(SRPSaveData.get(getOverworld()));
    }

    // GETTERS

    @ZenMethod("getPhase")
    public byte getPhase(int dim){
        return this.internal.getEvolutionPhase(dim);
    }

    @ZenMethod("getPoints")
    public int getPoints(int dim){
        return this.internal.getTotalKills(dim);
    }

    @ZenMethod("getCooldown")
    public int getCooldown(int dim){
        return this.internal.getCooldown(getOverworld(), dim);
    }

    @ZenMethod("getCanLose")
    public boolean getCanLose(int dim){
        return !this.internal.getCanLoss(dim);
    }

    @ZenMethod("getCanGain")
    public boolean getCanGain(int dim){
        return this.internal.getCanGain(dim);
    }

    @ZenMethod("getAssimilationCount")
    public int getAssimilationCount(int paraId){
        return this.internal.getNumberIDDataSpawn(paraId);
    }

    @ZenMethod("isEvoLocked")
    public boolean isEvoLocked(int paraId){
        return this.internal.checkParasiteID(paraId);
    }

    // SETTERS

    @ZenMethod("setPhase")
    public void setPhase(byte phase, int dim){
        this.internal.setEvolutionPhase(dim, phase, true, getOverworld(), true);
    }

    @ZenMethod("setPoints")
    public void setPoints(int points, int dim){
        this.internal.setTotalKills(points, dim, false, getOverworld(), true);
    }

    @ZenMethod("addPoints")
    public void addPoints(int points, int dim){
        this.internal.setTotalKills(points, dim, true, getOverworld(), true);
    }

    @ZenMethod("setCooldown")
    public void setCooldown(int cooldown, int dim){
        this.internal.setCooldown(cooldown, getOverworld(), dim);
    }

    @ZenMethod("addCooldown")
    public void addCooldown(int cooldown, int dim){
        int currCooldown = this.internal.getCooldown(getOverworld(), dim);
        this.internal.setCooldown(currCooldown + cooldown, getOverworld(), dim);
    }

    @ZenMethod("setCanLose")
    public void setCanLose(boolean newValue, int dim){
        this.internal.setLoss(!newValue, dim);
    }

    @ZenMethod("setCanGain")
    public void setCanGain(boolean newValue, int dim){
        this.internal.setGaining(newValue, dim);
    }

    @ZenMethod("increaseAssimilationCount")
    public void increaseAssimilationCount(int paraId){
        this.internal.addNumberIDDataSpawn(paraId);
    }

    @ZenMethod("unlockEvoLockForParasite")
    public void unlockEvoLockForParasite(int paraId){
        this.internal.unlockParasite(paraId);
    }

    @ZenMethod("resetEvoLock")
    public void resetEvoLock(){
        this.internal.resetLock();
    }
}
