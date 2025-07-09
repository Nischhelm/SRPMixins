package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasite;
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
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.handlers.SpawnPotentialsHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(SRPSpawning.class)
public abstract class SpawnListEntryGrabber {
    @Unique private static int[] srpmixins$phaseMinParasiteID = null;
    @Unique private static int[] srpmixins$phaseMaxParasiteID = null;

    @Unique
    private static boolean srpmixins$canSpawnInPhase(byte phase, byte type) {
        if(phase < 0) return false;
        if(SRPMixinsConfigHandler.morephases.enableMorePhases) {
            if (phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return false;

            return SRPMixinsConfigHandler.morephases.phaseMinParasiteID[phase] < type && type < SRPMixinsConfigHandler.morephases.phaseMaxParasiteID[phase];
        } else {
            if (phase > 10) return false;
            if(srpmixins$phaseMinParasiteID == null) {
                srpmixins$phaseMinParasiteID = new int[]{SRPConfigSystems.phaseCancelParasiteIDZero, SRPConfigSystems.phaseCancelParasiteIDOne, SRPConfigSystems.phaseCancelParasiteIDTwo, SRPConfigSystems.phaseCancelParasiteIDThree, SRPConfigSystems.phaseCancelParasiteIDFour, SRPConfigSystems.phaseCancelParasiteIDFive, SRPConfigSystems.phaseCancelParasiteIDSix, SRPConfigSystems.phaseCancelParasiteIDSeven, SRPConfigSystems.phaseCancelParasiteIDEight, SRPConfigSystems.phaseCancelParasiteIDNine, SRPConfigSystems.phaseCancelParasiteIDTen};
                srpmixins$phaseMaxParasiteID = new int[]{SRPConfigSystems.phaseMaxParasiteIDZero, SRPConfigSystems.phaseMaxParasiteIDOne, SRPConfigSystems.phaseMaxParasiteIDTwo, SRPConfigSystems.phaseMaxParasiteIDThree, SRPConfigSystems.phaseMaxParasiteIDFour, SRPConfigSystems.phaseMaxParasiteIDFive, SRPConfigSystems.phaseMaxParasiteIDSix, SRPConfigSystems.phaseMaxParasiteIDSeven, SRPConfigSystems.phaseMaxParasiteIDEight, SRPConfigSystems.phaseMaxParasiteIDNine, SRPConfigSystems.phaseMaxParasiteIDTen};
            }

            return srpmixins$phaseMinParasiteID[phase] < type && type < srpmixins$phaseMaxParasiteID[phase];
        }
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
        int paraId = SRPMixinsConfigProvider.mobNameToParaIdMap.getOrDefault(mobid, -1);
        if(paraId == -1) return false;

        if(biome instanceof BiomeParasite) SpawnPotentialsHandler.biomeSpawns.put(newEntry, paraId);

        else {
            //This whole part is only called if evo phase off or phase on and custom spawner off
            SpawnPotentialsHandler.allPhaseSpawns.put(newEntry, paraId);

            byte paraType = SRPMixinsConfigProvider.mobNameToParaTypeMap.getOrDefault(mobid, (byte) 0);
            if(SRPMixinsConfigHandler.spawns.fixColonyCarrierTypeId && paraId == 88) //colony_carrier
                paraType = (byte) 63;

            for(int phase = 0; phase <= (SRPMixinsConfigHandler.morephases.enableMorePhases ? SRPMixinsConfigHandler.morephases.maxEvolutionPhase : 10); phase++)
                if(srpmixins$canSpawnInPhase((byte) phase, paraType)) {
                    Map<Biome.SpawnListEntry, Integer> listPerPhase = SpawnPotentialsHandler.phaseIdSpawns.computeIfAbsent((byte) phase, HashMap::new);
                    listPerPhase.put(newEntry, paraId);
                }
        }

        return false; //don't add to original biome spawnlist
    }

    @ModifyExpressionValue(
            method = "removeInit",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    private static boolean srpmixins_resetSpawnLists(boolean useEvo){
        if(!useEvo){
            SpawnPotentialsHandler.resetCaches();
            srpmixins$phaseMinParasiteID = null;
            srpmixins$phaseMaxParasiteID = null;
        }
        return useEvo;
    }
}
