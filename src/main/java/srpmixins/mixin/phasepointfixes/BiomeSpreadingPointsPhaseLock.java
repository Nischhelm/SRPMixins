package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(ParasiteEventWorld.class)
public abstract class BiomeSpreadingPointsPhaseLock {

    @WrapWithCondition(
            method = {"canInfestBlock", "spreadBiomeBlockStain", "spreadBiomeBlockTrunk"},
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    private static boolean srpmixins_phaseLockBlock(SRPSaveData data, int id, int in, boolean plus, World world, boolean canChangePhase) {
        int startPhase = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
        return data.getEvolutionPhase(world.provider.getDimension()) >= startPhase;
    }
}