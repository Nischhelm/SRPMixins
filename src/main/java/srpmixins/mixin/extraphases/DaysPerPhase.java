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
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.ruleset.MinMaxDayPerPhaseRuleSet;
import srpmixins.util.IIsTicking;

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
    @Unique private final Map<Integer, Long> srpmixins$lastPhaseChangePlayerTick = new HashMap<>(); //for individual player tick, not world
    @Unique private World srpmixins$world_daysPerPhase = null;

    public DaysPerPhase(String name) {
        super(name);
    }

    @ModifyReturnValue(method = "get", at = @At("RETURN"), remap = false)
    private static SRPSaveData srpmixins_keepWorld(SRPSaveData original, World world){
        ((DaysPerPhase)(Object)original).srpmixins$world_daysPerPhase = world;
        return original;
    }

    @ModifyReturnValue(method = "writeToNBT", at = @At("TAIL"))
    private NBTTagCompound srpmixins_writeTicks(NBTTagCompound original){
        NBTTagList list = new NBTTagList();
        for(Map.Entry<Integer, Long> entry : srpmixins$lastPhaseChangeTick.entrySet()){
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("dimId", entry.getKey());
            nbt.setLong("ticks", entry.getValue());
            list.appendTag(nbt);
        }
        original.setTag("srpmixins_phaseTicks", list);

        if(SRPMixinsConfigHandler.playerphases.enabled && SRPMixinsConfigHandler.playerphases.individualTicks) {
            list = new NBTTagList();
            for(Map.Entry<Integer, Long> entry : srpmixins$lastPhaseChangePlayerTick.entrySet()){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("dimId", entry.getKey());
                nbt.setLong("ticks", entry.getValue());
                list.appendTag(nbt);
            }
            original.setTag("srpmixins_phasePlayerTicks", list);
            original.setLong("srpmixins_playerTickDistance", srpmixins$world_daysPerPhase.getTotalWorldTime() - ((IIsTicking) this).srpmixins$getTick());
        }

        return original;
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    private void srpmixins_readTicks(NBTTagCompound compound, CallbackInfo ci){
        if(compound.hasKey("srpmixins_phaseTicks")) {
            NBTTagList list = compound.getTagList("srpmixins_phaseTicks", 10);
            for (NBTBase nbt : list) {
                NBTTagCompound cmp = (NBTTagCompound) nbt;
                int dim = cmp.getInteger("dimId");
                long ticks = cmp.getLong("ticks");

                srpmixins$lastPhaseChangeTick.put(dim, ticks);
            }
        }

        if(compound.hasKey("srpmixins_phasePlayerTicks")) {
            NBTTagList list = compound.getTagList("srpmixins_phasePlayerTicks", 10);
            for (NBTBase nbt : list) {
                NBTTagCompound cmp = (NBTTagCompound) nbt;
                int dim = cmp.getInteger("dimId");
                long ticks = cmp.getLong("ticks");
                srpmixins$lastPhaseChangePlayerTick.put(dim, ticks);
            }
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
        if(MinMaxDayPerPhaseRuleSet.INSTANCE.hasNoRules()) return; //no rules set

        int idx = this.dimEPid.indexOf(dimId);
        if(idx == -1) return; //shouldn't happen
        byte currentPhase = this.dimEPevolution.get(idx);

        //No increase above max phase
        if(currentPhase >= SRPConfigProvider.getMaxPhase()) return;

        Map<String, Object> actualValues = new HashMap<>();
        actualValues.put("dim", dimId);
        actualValues.put("phase", currentPhase);

        int minDays = MinMaxDayPerPhaseRuleSet.INSTANCE.getTotalMin(actualValues);
        int maxDays = MinMaxDayPerPhaseRuleSet.INSTANCE.getTotalMax(actualValues);
        if(maxDays != 0 || minDays != Integer.MAX_VALUE) { // a rule exists for the current situation
            int days;
            if(SRPMixinsConfigHandler.playerphases.individualTicks) {
                long ticksElapsed = ((IIsTicking) this).srpmixins$getTick() - srpmixins$lastPhaseChangePlayerTick.getOrDefault(dimId, 0L);
                days = (int) (ticksElapsed / SRPConfig.dayTickValue);
            } else {
                long ticksElapsed = world.getWorldTime() - srpmixins$lastPhaseChangeTick.getOrDefault(dimId, 0L);
                days = (int) (ticksElapsed / SRPConfig.dayTickValue);
            }

            int newPoints = SRPConfigProvider.getPhaseMinPoints((byte) (currentPhase + 1));

            if (minDays != Integer.MAX_VALUE && days < minDays && pointsRef.get() >= newPoints) { // below min days
                canChangeRef.set(false);
                //Set points back to below limit
                pointsRef.set(newPoints - 1);
                this.dimEPtotalKills.set(idx, newPoints -1);
            }
            else if (maxDays != 0 && days >= maxDays) { // above max days
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

        if(SRPMixinsConfigHandler.playerphases.enabled && SRPMixinsConfigHandler.playerphases.individualTicks)
            srpmixins$lastPhaseChangePlayerTick.put(id, ((IIsTicking) this).srpmixins$getTick());
    }
}
