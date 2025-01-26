package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(ParasiteEventEntity.class)
public class ConvertImmuneFix {
    @ModifyVariable(
            method = "convertEntity",
            at = @At(value = "HEAD"),
            argsOnly = true,
            remap = false
    )
    private static boolean stopAssim(boolean in){
        return SRPMixinsConfigHandler.coth.stopCothImmuneAssim ? false: in;
    }

    @ModifyVariable(
            method = "convertEntityFeral",
            at = @At(value = "HEAD"),
            argsOnly = true,
            remap = false
    )
    private static boolean stopFeral(boolean in){
        return SRPMixinsConfigHandler.coth.stopCothImmuneFeral ? false: in;
    }
}
