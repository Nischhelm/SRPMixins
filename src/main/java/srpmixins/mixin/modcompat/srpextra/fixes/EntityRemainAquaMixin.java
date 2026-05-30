package srpmixins.mixin.modcompat.srpextra.fixes;

import energon.srpextra.entity.tile.EntityRemainAqua;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRemainAqua.class)
public abstract class EntityRemainAquaMixin {
    @Redirect(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lenergon/srpextra/entity/tile/EntityRemainAqua;damageAni:I", remap = false)
    )
    private int srpmixins_fixServerSideCrash(int original){
        return 0;
    }
}
