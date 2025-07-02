package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin {

    @ModifyConstant(
            method = "isValidLightLevelTwo",
            constant = @Constant(intValue = 7),
            remap = false
    )
    private int srpmixins_srparasitesEntityParasiteBase_isValidLightLevelTwo(int constant) {
        return 16;
    }
}