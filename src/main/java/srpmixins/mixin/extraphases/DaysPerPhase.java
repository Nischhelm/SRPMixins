package srpmixins.mixin.extraphases;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPConfigProvider;
import srpmixins.rules.MinMaxDayPerPhaseRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mixin(SRPSaveData.class)
public abstract class DaysPerPhase extends WorldSavedData {
    @Shadow(remap = false) private ArrayList<Integer> dimEPid;
    @Shadow(remap = false) private ArrayList<Byte> dimEPevolution;
    @Shadow(remap = false) private ArrayList<Integer> dimEPtotalKills;
    //Map<dimId, ticks>
    @Unique private final Map<Integer, Long> srpmixins$lastPhaseChangeTick = new HashMap<>();

    public DaysPerPhase(String name) {
        super(name);
    }

    @ModifyReturnValue(
            method = "writeToNBT",
            at = @At("TAIL")
    )
    private NBTTagCompound writeTicks(NBTTagCompound original){
        NBTTagList list = new NBTTagList();
        for(Map.Entry<Integer, Long> entry : srpmixins$lastPhaseChangeTick.entrySet()){
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("dimId", entry.getKey());
            nbt.setLong("ticks", entry.getValue());
            list.appendTag(nbt);
        }
        original.setTag("srpmixins_phaseTicks", list);
        return original;
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    private void readTicks(NBTTagCompound compound, CallbackInfo ci){
        if(!compound.hasKey("srpmixins_phaseTicks")) return;
        NBTTagList list = compound.getTagList("srpmixins_phaseTicks", 10);
        for(NBTBase nbt : list){
            NBTTagCompound cmp = (NBTTagCompound) nbt;
            int dim = cmp.getInteger("dimId");
            long ticks = cmp.getLong("ticks");
            srpmixins$lastPhaseChangeTick.put(dim, ticks);
        }
    }

    @Inject(
            method = "setTotalKills",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;phaseTenTotalPoints:I", ordinal = 0),
            remap = false
    )
    private void srpmixins_forceOrStopPhaseChange(
            int dimId, int addedPoints, boolean plus, World world, boolean canChangePhase,
            CallbackInfoReturnable<Boolean> cir,
            @Local(name = "kills") LocalIntRef pointsRef,
            @Local(argsOnly = true, ordinal = 1) LocalBooleanRef canChangeRef
    ){
        if(!canChangePhase) return; //shouldn't happen with phase increases
        if(!plus) return; //not for force
        if(addedPoints < 0) return; //not for reduction

        int idx = this.dimEPid.indexOf(dimId);
        if(idx == -1) return; //shouldn't happen
        byte currentPhase = this.dimEPevolution.get(idx);

        //No increase above max phase
        if(currentPhase >= SRPConfigProvider.getMaxPhase()) return;

        int minDays = MinMaxDayPerPhaseRule.getTotalMin(dimId, currentPhase);
        int maxDays = MinMaxDayPerPhaseRule.getTotalMax(dimId, currentPhase);
        if(maxDays != 0 || minDays != Integer.MAX_VALUE) { // a rule exists for the current situation
            long ticksElapsed = world.getWorldTime() - srpmixins$lastPhaseChangeTick.getOrDefault(dimId, 0L);
            int days = (int) (ticksElapsed / SRPConfig.dayTickValue);

            int newPoints = SRPConfigProvider.getPhaseMinPoints((byte) (currentPhase + 1));

            //SRPMixins.LOGGER.info("currDays {} minDays {} maxDays {} currPhase {} nextPhase {} nextPoints {}", days, minDays, maxDays, currentPhase, currentPhase + 1, newPoints);

            if (minDays != Integer.MAX_VALUE && days < minDays && pointsRef.get() >= newPoints) {
                canChangeRef.set(false);
                //Set points back to below limit
                pointsRef.set(newPoints - 1);
                this.dimEPtotalKills.set(idx, newPoints -1);
            }
            else if (maxDays != 0 && days >= maxDays) {
                pointsRef.set(newPoints + 1);
                this.dimEPtotalKills.set(idx, newPoints + 1);
            }
        }
    }

    @Inject(
            method = "setEvolutionPhase",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;checkForUnlock(BILnet/minecraft/world/World;)V"),
            remap = false
    )
    private void srpmixins_resetDays(int id, byte in, boolean override, World worldIn, boolean canChangePhase, CallbackInfoReturnable<Boolean> cir){
        srpmixins$lastPhaseChangeTick.put(id, worldIn.getWorldTime());
    }
}
