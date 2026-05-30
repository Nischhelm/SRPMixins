package srpmixins.mixin.modcompat.srpextra.fixes;

import energon.srpextra.entity.tile.EntityRemainAqua;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRemainAqua.class)
public abstract class EntityRemainAquaMixin {
    @SuppressWarnings("unused")
    public int damageAni = 5; //makes this also exist serverside, otherwise crash
}
