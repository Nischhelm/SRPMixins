package srpmixins.mixin.features.tendrilfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityBanoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityNoglaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityShycoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.focused.EntityShycoFocused;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityAnged;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityEsor;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import srpmixins.handlers.TendrilSyncHandler;

@Mixin(value = {
        EntityBanoAdapted.class,
        EntityCanraAdapted.class,
        EntityNoglaAdapted.class,
        EntityShycoAdapted.class,
        EntityShycoFocused.class,
        EntityAnged.class,
        EntityEsor.class
})
public abstract class ParaWithLeftRightTendril extends EntityParasiteBase implements TendrilSyncHandler.ISyncsTendrils {
    public ParaWithLeftRightTendril(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) private float leftTendrilHealth;
    @Shadow(remap = false) private float rightTendrilHealth;

    @Override
    public void srpmixins$onTracking(){
        if(this.leftTendrilHealth <= 0) this.world.setEntityState(this, (byte) 11);
        if(this.rightTendrilHealth <= 0) this.world.setEntityState(this, (byte) 22);
    }
}
