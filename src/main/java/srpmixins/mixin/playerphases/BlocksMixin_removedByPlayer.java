package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.*;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(value = {
        BlockFallingBase.class,
        BlockBase.class,
        BlockFallingInfestedStain.class,
        BlockInfestedRubble.class,
        BlockInfestedStain.class,
        BlockInfestedTrunk.class
})
public abstract class BlocksMixin_removedByPlayer {
    @Redirect(
            method="removedByPlayer",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id, @Local(argsOnly = true) EntityPlayer player){
        return SRPSaveDataInterface.get(world,player,null);
    }
}