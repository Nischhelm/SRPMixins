package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.*;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityCanra;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.ISummonsByUUID;
import srpmixins.util.configparse.Pair;

import java.util.*;

@Mixin(value = {
        EntityPPure.class,
        EntityPPreeminent.class,
        EntityPStationaryArchitect.class,
        EntityCanra.class,
        EntityCanraAdapted.class
})
public abstract class SummoningOverhaul extends EntityParasiteBase implements EntityCanSummon, ISummonsByUUID {
    public SummoningOverhaul(World worldIn) {
        super(worldIn);
    }

    @Unique private boolean srpmixins$hasReadConfig = false;
    @Unique private int srpmixins$maxSummons = 5; //TODO: idk what good default values are
    @Unique private int srpmixins$maxPoints = 5;

    @Unique private final Map<UUID, Integer> srpmixins$idToPointsMap = new HashMap<>();

    @Override
    public void srpmixins$addSummon(UUID id, int points) {
        if(this.world.isRemote) return; //the whole system should only run serverside

        if(!srpmixins$hasReadConfig){
            int paraId = this.getParasiteIDRegister();
            Pair<Integer, Integer> limits = SRPMixinsConfigProvider.summonLimits.get(paraId);
            if(limits != null){
                this.srpmixins$maxSummons = limits.getLeft();
                this.srpmixins$maxPoints = limits.getRight();
            } else {
                SRPMixins.LOGGER.warn("Using default summoning limits for parasite of type {}, no summon limits set up in SRPMixins spawning config at Summmoning Overhaul", SRPMobConfigProvider.paraIdToMobName.get(paraId));
            }

            srpmixins$hasReadConfig = true;
        }

        if(srpmixins$maxSummons >= 0 && srpmixins$idToPointsMap.size() >= srpmixins$maxSummons) return;
        if(srpmixins$maxPoints >= 0 && this.getActualParasites() >= srpmixins$maxPoints) return;
        srpmixins$idToPointsMap.put(id, points);
    }

    @Override
    public void checkID() { //Should be smth like updateSummonList
        if(this.world.isRemote) return; //the whole system should only run serverside

        Set<UUID> toRemove = new HashSet<>();
        srpmixins$idToPointsMap.forEach((id, pt) -> {
            if (((WorldServer) this.world).getEntityFromUuid(id) == null) {
                toRemove.add(id);
                setActualParasites(-pt);
            }
        });
        toRemove.forEach(srpmixins$idToPointsMap::remove);
    }

    @Override
    public Map<UUID, Integer> srpmixins$getSummonEntries(){
        return this.srpmixins$idToPointsMap;
    }
}
