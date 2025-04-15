package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityVesta;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityVesta.class)
public abstract class ColonyCarrierTypeIdFix extends EntityParasiteBase {
    public ColonyCarrierTypeIdFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "<init>",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void srpmixins_fixColCarrierTypeId(World worldIn, CallbackInfo ci){
        this.type = 63;
    }
}
