package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAta.class)
public abstract class GnatNetworkFix extends Entity {
    public GnatNetworkFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/monster/inborn/EntityAta;lifespan:I", remap = false, ordinal = 0),
            cancellable = true
    )
    private void srpmixins_fixGnatClientNetwork(CallbackInfo ci){
        if(this.world.isRemote) ci.cancel();
    }
}
