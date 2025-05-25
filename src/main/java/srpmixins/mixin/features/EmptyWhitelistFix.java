package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParasiteEventEntity.class)
public abstract class EmptyWhitelistFix {

    // Fix the specific case where an empty config list (ex Damage Cap Blacklists) with the whitelist flag is always treated as a blacklist
    @ModifyReturnValue(
            method = "checkName",
            at = @At(value = "RETURN", ordinal = 2),
            remap = false
    )
    private static boolean srpmixins_checkNameInvertEmpty(boolean original, @Local(argsOnly = true) boolean inverted){
        return original || inverted;
    }

    /*
    Wish i could just do this

     * Method should be called "isEntryBlacklisted", or "doesEntryContainAnyOfTheseStrings?"
     * @author Nischhelm
     * @reason optimise and fix empty whitelist being read as empty blacklist.

    @Overwrite(remap = false)
    public static boolean checkName(String potentialElement, String[] blacklist, boolean isWhitelist) {
        boolean elementContainsAnyListEntry = Arrays.stream(blacklist).anyMatch(potentialElement::contains);
        return elementContainsAnyListEntry != isWhitelist;
    }*/
}
