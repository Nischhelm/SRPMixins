package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin_DamageWeakness {
    @Unique private static Map<String, Float> srpmixins$parasiteWeakToItems = null;
    @Unique private static Map<String, Float> srpmixins$parasiteWeakToMobs = null;
    @Unique private static Map<String, Float> srpmixins$parasiteWeakToElse = null;
    @Unique private static float srpmixins$getMultiplier(String damageName, int type) {
        if (type == 1 && srpmixins$parasiteWeakToItems == null) {
            srpmixins$parasiteWeakToItems = new HashMap<>();
            for (String s : SRPConfig.parasiteWeakToItems) {
                String[] split = s.split(";");
                String dmgName = split[0];
                float multi = Float.parseFloat(split[1]);
                srpmixins$parasiteWeakToItems.put(dmgName, multi);
            }
        }
        else if (type == 2 && srpmixins$parasiteWeakToMobs == null) {
            srpmixins$parasiteWeakToMobs = new HashMap<>();
            for (String s : SRPConfig.parasiteWeakToMobs) {
                String[] split = s.split(";");
                String dmgName = split[0];
                float multi = Float.parseFloat(split[1]);
                srpmixins$parasiteWeakToMobs.put(dmgName, multi);
            }
        }
        else if (type == 3 && srpmixins$parasiteWeakToElse == null) {
            srpmixins$parasiteWeakToElse = new HashMap<>();
            for (String s : SRPConfig.parasiteWeakToElse) {
                String[] split = s.split(";");
                String dmgName = split[0];
                float multi = Float.parseFloat(split[1]);
                srpmixins$parasiteWeakToElse.put(dmgName, multi);
            }
        }

        switch (type) {
            case 1: return srpmixins$parasiteWeakToItems.get(damageName);
            case 2: return srpmixins$parasiteWeakToMobs.get(damageName);
            case 3: return srpmixins$parasiteWeakToElse.get(damageName);
            default: return 1.0F;
        }
    }

    @ModifyVariable(
            method = "attackEntityFrom",
            at = @At(value = "LOAD", ordinal = 1),
            name = "flagWeak"
    )
    private boolean srpmixins_overwriteDamageBlacklists(
            boolean value, @Local(argsOnly = true) LocalFloatRef amount,
            @Local(name = "damageName") String damageName,
            @Local(name = "naniDesu") byte damageType
    ){
        amount.set(amount.get() * srpmixins$getMultiplier(damageName, damageType));

        //Don't run original if-bracket
        return false;
    }

}
