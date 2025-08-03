package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.*;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityCanra;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.util.ISummonsByUUID;

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
    @Unique private int srpmixins$maxPoints = 5;

    @Unique private final Map<UUID, Integer> srpmixins$idToPointsMap = new HashMap<>();

    @Override
    public void srpmixins$addSummon(UUID id, int points) {
        if(this.world.isRemote) return; //the whole system should only run serverside

        if(!this.srpmixins$hasReadConfig){
            int paraId = this.getParasiteIDRegister();
            this.srpmixins$maxPoints = SRPMixinsConfigProvider.summonLimits.getOrDefault(paraId, 3);
            this.srpmixins$hasReadConfig = true;
        }

        if(this.srpmixins$maxPoints >= 0 && this.getActualParasites() >= this.srpmixins$maxPoints) return;
        this.srpmixins$idToPointsMap.put(id, points);
    }

    @Override
    public void checkID() { //Should be smth like updateSummonList
        if(this.world.isRemote) return; //the whole system should only run serverside

        Set<UUID> toRemove = new HashSet<>();
        this.srpmixins$idToPointsMap.forEach((id, pt) -> {
            if (((WorldServer) this.world).getEntityFromUuid(id) == null) {
                toRemove.add(id);
                setActualParasites(-pt);
            }
        });
        toRemove.forEach(this.srpmixins$idToPointsMap::remove);
    }

    @Override
    public Map<UUID, Integer> srpmixins$getSummonEntries(){
        return this.srpmixins$idToPointsMap;
    }
}
