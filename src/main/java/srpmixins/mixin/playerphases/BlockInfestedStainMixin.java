package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedStain;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(BlockInfestedStain.class)
public abstract class BlockInfestedStainMixin {
    @Redirect(
            method="removedByPlayer",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin(World world, @Local(argsOnly = true) EntityPlayer player){
        return SRPSaveDataInterface.get(world,player,null);
    }
}