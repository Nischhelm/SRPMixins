package srpmixins.mixin.modcompat.lostcitytweaks.bloodmoon;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.sugar.Local;
import lumien.bloodmoon.server.BloodmoonSpawner;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.event.WorldMobCapEvent;
import srpmixins.util.ParasiteCreatureType;

import java.util.Set;

@Mixin(BloodmoonSpawner.class)
public abstract class BloodmoonSpawnerMixin {
    @Shadow(remap = false) @Final private Set<ChunkPos> eligibleChunksForSpawning;

    @Inject(
            method = "findChunksForSpawning",
            at = @At("HEAD"),
            remap = false
    )
    private void srpmixins_toggleParaSpawningOn(CallbackInfoReturnable<Integer> cir){
        ParasiteCreatureType.paraMobCapToggle = 1;
    }

    @Inject(
            method = "findChunksForSpawning",
            at = @At("RETURN"),
            remap = false
    )
    private void srpmixins_toggleParaSpawningOff(CallbackInfoReturnable<Integer> cir){
        ParasiteCreatureType.paraMobCapToggle = 0;
    }

    @ModifyVariable(
            method = "findChunksForSpawning",
            at = @At(value = "STORE", ordinal = 1),
            name = "spawnLimit",
            remap = false
    )
    private int srpmixins_setParasiteMobCapPerPlayer(
            int originalMobCap,
            @Local EnumCreatureType type,
            @Local(argsOnly = true) WorldServer world,
            @Local(name = "k4") int countedMobs,
            @Local(name = "j4") int countAlreadySpawned
    ) {
        int currMobCap = type != ParasiteCreatureType.PARASITE ? originalMobCap : SRPConfig.worldMobCap + SRPConfig.worldMobCapPlusPlayer * ParasiteCreatureType.PARASITE.getMaxNumberOfCreature();
        WorldMobCapEvent event = new WorldMobCapEvent(world, type, this.eligibleChunksForSpawning, countedMobs, countAlreadySpawned, currMobCap);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getMobCap();
    }
}
