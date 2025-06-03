package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.tile.TileEntityDod;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(TileEntityDod.class)
public abstract class TileEntityDodMixin extends TileEntity {
    @Redirect(
            method="update",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;", remap = false)
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id){
        return SRPSaveDataInterface.get(world,null,this.getPos());
    }

}