package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(ParasiteEventWorld.class)
public abstract class BiomeSpreadingPointsPhaseLock {

    @Redirect(
            method="canInfestBlock",
            at=@At(value= "FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;valueBlock:I"),
            remap=false
    )
    //done like this to not conflict with SRPCortesia
    private static int phaseLockBlock(@Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos blockPos){
        int startPhase = SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase;
        SRPSaveData data = SRPSaveDataInterface.get(world, null, blockPos);
        if(startPhase>-1 && data.getEvolutionPhase(world.provider.getDimension())<startPhase)
            return 0;
        return SRPConfigSystems.valueBlock;
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