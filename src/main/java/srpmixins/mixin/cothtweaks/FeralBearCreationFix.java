package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPFeral;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerBear;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfBear;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityInfBear.class)
public abstract class FeralBearCreationFix extends EntityPInfected {
    public FeralBearCreationFix(World worldIn) {
        super(worldIn);
    }

    @Override
    public EntityPFeral getFeral() {
        return new EntityFerBear(this.world);
    }
}
