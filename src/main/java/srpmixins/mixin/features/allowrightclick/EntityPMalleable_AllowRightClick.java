package srpmixins.mixin.features.allowrightclick;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPMalleable.class)
public abstract class EntityPMalleable_AllowRightClick extends EntityParasiteBase {
    public EntityPMalleable_AllowRightClick(World worldIn) {
        super(worldIn);
    }

    // Replace client always returning true with a super call
    @ModifyReturnValue(
            method = "processInteract",
            at = @At(value = "RETURN", ordinal = 0)
    )
    public boolean srpmixins_rightClickAdapted(boolean original, EntityPlayer player, EnumHand hand){
        return super.processInteract(player, hand);
    }
}
