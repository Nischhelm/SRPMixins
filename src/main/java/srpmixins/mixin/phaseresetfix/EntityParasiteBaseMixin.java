package srpmixins.mixin.phaseresetfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin extends EntityLiving {
    public EntityParasiteBaseMixin(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method = "onInitialSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    public boolean skipIfClient(){
        return !this.world.isRemote && SRPConfigSystems.useEvolution;
    }
}