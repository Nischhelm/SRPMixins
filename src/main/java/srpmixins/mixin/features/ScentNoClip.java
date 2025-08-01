package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityParasiticScent.class)
public abstract class ScentNoClip extends Entity{
    public ScentNoClip(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;)V",
            at = @At(value = "TAIL")
    )
    private void srpmixins_scentsDontClip(World worldIn, CallbackInfo ci){
        this.noClip = true;
    }
}
