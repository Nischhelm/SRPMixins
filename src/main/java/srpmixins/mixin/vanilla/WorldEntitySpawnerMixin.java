package srpmixins.mixin.vanilla;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.event.WorldMobCapEvent;
import srpmixins.util.ParasiteCreatureType;

import java.util.Set;

@Mixin(WorldEntitySpawner.class)
public abstract class WorldEntitySpawnerMixin {
    @Shadow @Final private Set<ChunkPos> eligibleChunksForSpawning;

    @ModifyVariable(
            method = "findChunksForSpawning",
            at = @At("STORE"),
            name = "l4"
    )
    private int srpmixins_setParasiteMobCapPerPlayer(
            int original,
            @Local EnumCreatureType type,
            @Local(argsOnly = true) WorldServer world,
            @Local(name = "k4") int countedMobs,
            @Local(name = "j4") int countAlreadySpawned
    ) {
        int currMobCap = type != ParasiteCreatureType.PARASITE ? original : SRPConfig.worldMobCap + SRPConfig.worldMobCapPlusPlayer * original;
        WorldMobCapEvent event = new WorldMobCapEvent(world, type, this.eligibleChunksForSpawning, countedMobs, countAlreadySpawned, currMobCap);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getMobCap();
    }
}
