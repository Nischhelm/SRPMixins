package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedTrunk;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.util.SRPSaveDataInterface;

@Mixin(BlockInfestedTrunk.class)
public abstract class BlockInfestedTrunkMixin {

    @Unique
    EntityPlayer player;

    @Inject(
            method = "removedByPlayer",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void mixin(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, CallbackInfoReturnable<Boolean> cir){
        this.player = player;
    }

    @Redirect(
            method="removedByPlayer",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,player,null);
    }
}