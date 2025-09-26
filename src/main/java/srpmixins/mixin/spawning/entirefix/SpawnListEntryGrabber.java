package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.handlers.SpawnPotentialsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(SRPSpawning.class)
public abstract class SpawnListEntryGrabber {
    @Unique private static int[] srpmixins$phaseMinParasiteID = null;
    @Unique private static int[] srpmixins$phaseMaxParasiteID = null;

    @Unique
    private static boolean srpmixins$canSpawnInPhase(byte phase, byte type) {
        if(phase < 0) return false;
        if(phase > SRPConfigProvider.getMaxPhase()) return false;
        if(SRPMixinsConfigHandler.morephases.enableMorePhases) {
            return SRPMixinsConfigHandler.morephases.phaseMinParasiteID[phase] < type && type < SRPMixinsConfigHandler.morephases.phaseMaxParasiteID[phase];
        } else {
            if(srpmixins$phaseMinParasiteID == null) {
                srpmixins$phaseMinParasiteID = new int[]{SRPConfigSystems.phaseCancelParasiteIDZero, SRPConfigSystems.phaseCancelParasiteIDOne, SRPConfigSystems.phaseCancelParasiteIDTwo, SRPConfigSystems.phaseCancelParasiteIDThree, SRPConfigSystems.phaseCancelParasiteIDFour, SRPConfigSystems.phaseCancelParasiteIDFive, SRPConfigSystems.phaseCancelParasiteIDSix, SRPConfigSystems.phaseCancelParasiteIDSeven, SRPConfigSystems.phaseCancelParasiteIDEight, SRPConfigSystems.phaseCancelParasiteIDNine, SRPConfigSystems.phaseCancelParasiteIDTen};
                srpmixins$phaseMaxParasiteID = new int[]{SRPConfigSystems.phaseMaxParasiteIDZero, SRPConfigSystems.phaseMaxParasiteIDOne, SRPConfigSystems.phaseMaxParasiteIDTwo, SRPConfigSystems.phaseMaxParasiteIDThree, SRPConfigSystems.phaseMaxParasiteIDFour, SRPConfigSystems.phaseMaxParasiteIDFive, SRPConfigSystems.phaseMaxParasiteIDSix, SRPConfigSystems.phaseMaxParasiteIDSeven, SRPConfigSystems.phaseMaxParasiteIDEight, SRPConfigSystems.phaseMaxParasiteIDNine, SRPConfigSystems.phaseMaxParasiteIDTen};
            }

            return srpmixins$phaseMinParasiteID[phase] < type && type < srpmixins$phaseMaxParasiteID[phase];
        }
    }

    @Unique private static boolean srpmixins$hasDoneFirstBiome = false;

    @WrapOperation(
            method = "init",
            at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"),
            remap = false
    )
    private static boolean srpmixins_onlyRegisterForFirstBiome(Iterator<?> instance, Operation<Boolean> original){
        if(!srpmixins$hasDoneFirstBiome){
            srpmixins$hasDoneFirstBiome = true;
            return original.call(instance); //returns true except if there are no biomes, but that should never happen
        }
        return false;
    }

    @Inject(
            method = "init",
            at = @At("TAIL"),
            remap = false
    )
    private static void srpmixins_resetFlag(CallbackInfo ci){
        srpmixins$hasDoneFirstBiome = false;
    }

    @WrapOperation(
            method = "addSpawn",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            remap = false
    )
    private static boolean srpmixins_grabSpawnListEntries(List instance, Object e, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) int type, @Local(argsOnly = true) Biome biome){
        if(type != 0) return original.call(instance, e); //this never happens in base SRP, registering non hostile parasites
        Biome.SpawnListEntry newEntry = (Biome.SpawnListEntry) e;

        //Get parasite id
        ResourceLocation loc = EntityList.getKey(newEntry.entityClass);
        if(loc == null) return false;
        String mobid = loc.getPath();
        int paraId = SRPMobConfigProvider.mobNameToParaIdMap.getOrDefault(mobid, Integer.MIN_VALUE);
        if(paraId == Integer.MIN_VALUE) return false;

        //This is intended to be equality check, not instanceof
        if(biome == SRPBiomes.biomeInfested) SpawnPotentialsHandler.biomeSpawns.add(new SpawnPotentialsHandler.BiomeSpawnListEntryWrapper(newEntry, paraId));

        else {
            //This whole part is only called if evo phase off or phase on and custom spawner off
            SpawnPotentialsHandler.allPhaseSpawns.add(new SpawnPotentialsHandler.BiomeSpawnListEntryWrapper(newEntry, paraId));

            byte paraType = SRPMobConfigProvider.mobNameToParaTypeMap.getOrDefault(mobid, (byte) 0);

            for(int phase = 0; phase <= SRPConfigProvider.getMaxPhase(); phase++)
                if(srpmixins$canSpawnInPhase((byte) phase, paraType)) {
                    List<SpawnPotentialsHandler.BiomeSpawnListEntryWrapper> listPerPhase = SpawnPotentialsHandler.phaseIdSpawns.computeIfAbsent((byte) phase, ArrayList::new);
                    listPerPhase.add(new SpawnPotentialsHandler.BiomeSpawnListEntryWrapper(newEntry, paraId));
                }
        }

        return false; //don't add to original biome spawnlist
    }

    @Inject(
            method = "init",
            at = @At("TAIL"),
            remap = false
    )
    private static void srpmixins_clearOwnCache(CallbackInfo ci){
        srpmixins$phaseMinParasiteID = null;
        srpmixins$phaseMaxParasiteID = null;
    }

    @ModifyExpressionValue(
            method = "removeInit",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    private static boolean srpmixins_resetSpawnLists(boolean useEvo){
        if(!useEvo) SpawnPotentialsHandler.resetCaches();
        return useEvo;
    }
}
