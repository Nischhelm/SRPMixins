package srpmixins.mixin.phaseresetfix.srpsavedata;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin extends EntityLiving {
    public EntityParasiteBaseMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "onInitialSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z", remap = false)
    )
    public boolean srpmixins_skipIfClient(boolean original){
        return !this.world.isRemote && original;
    }
}