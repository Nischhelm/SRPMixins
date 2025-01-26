package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(ParasiteEventWorld.class)
public abstract class BiomeSpreadingPointsPhaseLock {

    @Redirect(
            method="canInfestBlock",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    private static boolean phaseLockBlock(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int startPhase = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
        if(startPhase>-1 && data.getEvolutionPhase(id)<startPhase)
            return false;
        return data.setTotalKills(id,in,plus,worldIn,canChangePhase);
    }

    @Redirect(
            method="spreadBiomeBlockTrunk",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    private static boolean phaseLockTrunk(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int startPhase = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
        if(startPhase>-1 && data.getEvolutionPhase(id)<startPhase)
            return false;
        return data.setTotalKills(id,in,plus,worldIn,canChangePhase);
    }

    @Redirect(
            method="spreadBiomeBlockStain",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    private static boolean phaseLockStain(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int startPhase = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
        if(startPhase>-1 && data.getEvolutionPhase(id)<startPhase)
            return false;
        return data.setTotalKills(id,in,plus,worldIn,canChangePhase);
    }
}