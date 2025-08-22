package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.*;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityCanra;
import net.minecraft.entity.Entity;
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

    @Unique private int srpmixins$maxPoints = Integer.MAX_VALUE;
    @Unique private final Map<UUID, Integer> srpmixins$idToPointsMap = new HashMap<>();

    @Override
    public void srpmixins$addSummon(UUID id, int points) { //instead of addId
        if(this.world.isRemote) return; //the whole system should only run serverside

        if(this.srpmixins$maxPoints == Integer.MAX_VALUE) {
            int paraId = this.getParasiteIDRegister();
            this.srpmixins$maxPoints = SRPMixinsConfigProvider.summonLimits.getOrDefault(paraId, 3);
        }

        this.srpmixins$idToPointsMap.put(id, points);
    }

    @Unique
    public void srpmixins$updateSummonList(){
        if(this.world.isRemote) return; //the whole system should only run serverside

        Set<UUID> toRemove = new HashSet<>();
        this.srpmixins$idToPointsMap.forEach((id, pt) -> {
            Entity entity = ((WorldServer) this.world).getEntityFromUuid(id);
            if (entity == null || entity.isDead) toRemove.add(id);
        });

        toRemove.forEach(this.srpmixins$idToPointsMap::remove);
    }

    @Override
    public int getTotalParasites() {
        return srpmixins$maxPoints;
    }

    @Override
    public int getActualParasites() {
        srpmixins$updateSummonList();

        int sum = 0;
        for(int entry : srpmixins$idToPointsMap.values()) sum += entry;
        return sum;
    }

    @Override
    public void setActualParasites(int addedPoints) {
        //no op, done via addSummon
    }

    @Override
    public void checkID() {
        //no op, done via updateSummonList
    }

    @Override
    public Map<UUID, Integer> srpmixins$getSummonEntries(){
        return this.srpmixins$idToPointsMap;
    }
}
