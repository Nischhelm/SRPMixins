package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrolSIV;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityAINexusGrow.class)
public abstract class BeckonUpgradeDeny {
    @Redirect(
            method = "upgradeV",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventWorld;canBiomeStillExist(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)I"),
            remap = false
    )
    int blockNewStage4Beckon(World world, BlockPos pos, boolean spread){
        int whateverSRPdoes = ParasiteEventWorld.canBiomeStillExist(world,pos,spread);
        if(SRPMixinsConfigHandler.deterrents.limitStage4Beckons) {
            boolean stage4BeckonIsNearby = !world.getEntitiesWithinAABB(EntityVenkrolSIV.class, new AxisAlignedBB(pos).grow(20)).isEmpty();
            return stage4BeckonIsNearby ? 999 : whateverSRPdoes;
        }
        return whateverSRPdoes;
    }
}