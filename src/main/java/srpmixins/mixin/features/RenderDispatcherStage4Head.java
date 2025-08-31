package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.EntityBodyModel;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityDodSIV;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(EntityDodSIV.class)
public abstract class RenderDispatcherStage4Head extends Entity {
    public RenderDispatcherStage4Head(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) private EntityBodyModel head;
    @Unique private Entity[] srpmixins$parts = null;

    @Override
    @Nullable
    public Entity[] getParts() {
        if(srpmixins$parts == null) srpmixins$parts = new Entity[]{this.head};
        return srpmixins$parts;
    }
}
