package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(EntityPAdapted.class)
public abstract class EntityPAdaptedMixin extends Entity {
    public EntityPAdaptedMixin(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method="func_70623_bb",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,null,this.getPosition());
    }
}