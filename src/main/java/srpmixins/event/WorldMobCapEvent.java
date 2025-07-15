package srpmixins.event;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Set;

public class WorldMobCapEvent extends WorldEvent {
    private final EnumCreatureType type;
    private final int countedMobs;
    private final int countAlreadySpawned;
    private final Set<ChunkPos> eligibleChunks;

    private int mobCap;

    public WorldMobCapEvent(World world, EnumCreatureType type, Set<ChunkPos> eligibleChunks, int countedMobs, int countAlreadySpawned, int mobCap) {
        super(world);
        this.type = type;
        this.eligibleChunks = eligibleChunks;
        this.countedMobs = countedMobs;
        this.countAlreadySpawned = countAlreadySpawned;

        this.mobCap = mobCap;
    }

    public EnumCreatureType getType() {
        return type;
    }

    public Set<ChunkPos> getEligibleChunks() {
        return eligibleChunks;
    }

    public int getCountedMobs() {
        return countedMobs;
    }

    public int getCountAlreadySpawned() {
        return countAlreadySpawned;
    }

    public int getMobCap() {
        return mobCap;
    }

    public void setMobCap(int mobCap) {
        this.mobCap = mobCap;
    }
}
