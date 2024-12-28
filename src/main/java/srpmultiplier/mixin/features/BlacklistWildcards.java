
package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(ParasiteEventEntity.class)
public abstract class BlacklistWildcards {
    @Inject(
            method = "checkName",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    private static void wildcard(String name, String[] list, boolean inverted, CallbackInfoReturnable<Boolean> cir){
        //SRP checkName function returns true if name is in blacklist (or name is not in whitelist)
        //Name could be "isBlacklisted"/"isNotWhitelisted"
        if(SRPMultiplierConfigHandler.server.blacklistsWildcardable && name.contains(":")) {
            String modName = name.split(":")[0] + ":*";

            for (String line : list) {
                if (line.endsWith(":*")) {
                    if (modName.equals(line)) {
                        cir.setReturnValue(!inverted);
                        return;
                    }
                } else {
                    if (name.equals(line)){
                        cir.setReturnValue(!inverted);
                        return;
                    }
                }
            }

            cir.setReturnValue(inverted);
        }
    }
}