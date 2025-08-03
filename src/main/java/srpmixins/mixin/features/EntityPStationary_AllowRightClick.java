package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityDodT;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityNak;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityDodT.class,
        EntityNak.class,
})
public abstract class EntityPStationary_AllowRightClick extends EntityPStationary {
    public EntityPStationary_AllowRightClick(World worldIn) {
        super(worldIn);
    }

    // Replace client always returning true with a super call
    @ModifyReturnValue(
            method = "processInteract",
            at = @At(value = "RETURN", ordinal = 0)
    )
    public boolean srpmixins_rightClickStationary(boolean original, EntityPlayer player, EnumHand hand){
        return super.processInteract(player, hand);
    }
}
