package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.EntityRemain;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(EntityRemain.class)
public abstract class EntityRemainMixin extends Entity {
    public EntityRemainMixin(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method="onUpdate",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id){
        return SRPSaveDataInterface.get(world, null,this.getPosition());
    }
}