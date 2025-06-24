package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityWave;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityWaveShock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(value = {
        EntityWaveShock.class,
        EntityWave.class
})
public abstract class EntityWaveShockAIMixin extends EntityParasiteBase {
    public EntityWaveShockAIMixin(World worldIn) {
        super(worldIn);
    }

    @Override
    public void applyBonuses(int colony, int node, byte phase) {
        //don't add extra targeting AIs
        //also skips colony and node bonus but those attributes don't matter for wave entities anyway
    }
}
