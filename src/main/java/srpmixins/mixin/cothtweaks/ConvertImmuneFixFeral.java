package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ParasiteEventEntity.class)
public class ConvertImmuneFixFeral {
    @ModifyVariable(
            method = "convertEntityFeral",
            at = @At(value = "HEAD"),
            argsOnly = true,
            remap = false
    )
    private static boolean stopFeral(boolean in){
        return false;
    }
}
