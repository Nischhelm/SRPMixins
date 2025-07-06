package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import org.spongepowered.asm.mixin.Mixin;
import srpmixins.util.IParasite;

@Mixin(EntityParasiteBase.class)
public class EntityParasiteBaseMixin implements IParasite {
    //Literally does nothing more than add IParasite to EntityParasiteBase for spawn logic
}
