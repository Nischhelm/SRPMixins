package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.tile.TileEntityCanister;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.util.SRPSaveDataInterface;

@Mixin(TileEntityCanister.class)
public abstract class TileEntityCanisterMixin extends TileEntity {

    @Redirect(
            method="func_73660_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,null,this.getPos());
    }

}