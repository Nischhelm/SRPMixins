package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityDodT;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityNak;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityDodT.class,
        EntityNak.class,
        EntityPMalleable.class
})
public abstract class EntityParasiteBase_AllowRightClick extends EntityParasiteBase {

    public EntityParasiteBase_AllowRightClick(World worldIn) {
        super(worldIn);
    }

    // Replace client always returning true with a super call
    @ModifyReturnValue(
            method = "processInteract",
            at = @At(value = "RETURN", ordinal = 0)
    )
    public boolean srpmixins_rightClickAdapted(boolean original, @Local(argsOnly = true) EntityPlayer player, @Local(argsOnly = true) EnumHand hand){
        return super.processInteract(player, hand);
    }
}
