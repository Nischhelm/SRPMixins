package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ParasiteEventEntity.class)
public class ConvertImmuneFixAssim {
    @ModifyVariable(
            method = "convertEntity",
            at = @At(value = "HEAD"),
            argsOnly = true,
            remap = false
    )
    private static boolean stopAssim(boolean in){
        return false;
    }
}
