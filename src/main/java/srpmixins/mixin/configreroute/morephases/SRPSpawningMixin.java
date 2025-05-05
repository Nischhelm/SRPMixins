package srpmixins.mixin.configreroute.morephases;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(SRPSpawning.class)
public abstract class SRPSpawningMixin {
    @Unique private static final Map<Byte, List<Biome.SpawnListEntry>> srpmixins$spawnLists = new HashMap<>();

    @WrapMethod(method = "getSpawns", remap = false)
    private static List<Biome.SpawnListEntry> srpmixins_getSpawns(byte phase, Operation<List<Biome.SpawnListEntry>> original){
        return srpmixins$spawnLists.get(phase);
    }

    @Inject(
            method = "init",
            at = @At(value = "NEW", target = "()Ljava/util/ArrayList;", ordinal = 0),
            remap = false,
            cancellable = true
    )
    @SuppressWarnings("unchecked")
    private static void srpmixins_initSpawnLists(CallbackInfo ci){
        for(String s : SRPMixinsConfigHandler.morephases.phaseSpawnList) {
            String[] split = s.split(";");
            if(split.length < 5){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Spawn List entry, too few arguments (expected 6): {}", s);
                continue;
            }

            ResourceLocation mobId = new ResourceLocation(split[1].trim());
            Class<? extends Entity> mobClass = EntityList.getClass(mobId);
            if(mobClass == null || !EntityLiving.class.isAssignableFrom(mobClass)){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Spawn List entry, mob doesn't exist or is not EntityLiving: {}", mobId.toString());
                continue;
            }

            //Pattern: [0 - 2; 4; 10; ...]
            String phasesToSpawnIn = split[0].trim().replace("[","").replace("]","");
            String[] split2 = phasesToSpawnIn.split(",");
            List<Byte> spawnPhases = new ArrayList<>();

            try {
                for(String s2 : split2){
                    //Pattern: min - max
                    if(s2.contains("-")){
                        String[] split3 = s2.split("-");
                        if(split3.length < 2){
                            SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Spawn List entry, phase list pattern incorrect, expected minPhase - maxPhase, provided was: {}", s2);
                            continue;
                        }
                        byte minPhase = Byte.parseByte(split3[0].trim());
                        byte maxPhase = Byte.parseByte(split3[1].trim());
                        for(byte i = minPhase; i <= maxPhase; i++)
                            spawnPhases.add(i);
                    //Pattern: specificPhase
                    } else
                        spawnPhases.add(Byte.parseByte(s2.trim()));
                }

                int minGroup = Integer.parseInt(split[2].trim());
                int maxGroup = Integer.parseInt(split[3].trim());
                int weight = Integer.parseInt(split[4].trim());

                Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) mobClass, weight, minGroup, maxGroup);

                for (byte i : spawnPhases) {
                    srpmixins$spawnLists.computeIfAbsent(i, k -> new ArrayList<>());
                    srpmixins$spawnLists.get(i).add(entry);
                }
            } catch (Exception e) {
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Spawn List entry, expected pattern minPhase; maxPhase; modid:mobName; minGroupCount; maxGroupCount; spawnWeight. Provided was: {}", s);
            }
        }
        ci.cancel();
    }
}
