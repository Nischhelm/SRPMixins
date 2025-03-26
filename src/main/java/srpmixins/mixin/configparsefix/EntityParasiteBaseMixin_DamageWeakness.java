package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import org.spongepowered.asm.mixin.Debug;
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
        switch (type) {
            case 1:
                if (srpmixins$parasiteWeakToItems == null) srpmixins$parasiteWeakToItems = srpmixins$parseWeaknessList(SRPConfig.parasiteWeakToItems);
                return srpmixins$parasiteWeakToItems.getOrDefault(damageName, 1.0F);
            case 2:
                if (srpmixins$parasiteWeakToMobs == null) srpmixins$parasiteWeakToMobs = srpmixins$parseWeaknessList(SRPConfig.parasiteWeakToMobs);
                return srpmixins$parasiteWeakToMobs.getOrDefault(damageName, 1.0F);
            case 3:
                if (srpmixins$parasiteWeakToElse == null) srpmixins$parasiteWeakToElse = srpmixins$parseWeaknessList(SRPConfig.parasiteWeakToElse);
                return srpmixins$parasiteWeakToElse.getOrDefault(damageName, 1.0F);
            default:
                return 1.0F;
        }
    }

    @Unique
    private static Map<String, Float> srpmixins$parseWeaknessList(String[] list){
        Map<String, Float> map = new HashMap<>();
        for (String s : list) {
            String[] split = s.split(";");
            String dmgName = split[0];
            float multi = Float.parseFloat(split[1]);
            map.put(dmgName, multi);
        }
        return map;
    }

    @ModifyVariable(
            method = "attackEntityFrom",
            at = @At(value = "LOAD", ordinal = 1),
            name = "flagWeak"
    )
    private boolean srpmixins_overwriteDamageWeakness(
            boolean value,
            @Local(argsOnly = true) LocalFloatRef amount,
            @Local(name = "damageName") String damageName,
            @Local(name = "naniDesu") byte damageType
    ){
        amount.set(amount.get() * srpmixins$getMultiplier(damageName, damageType));

        //Don't run original if-bracket
        return false;
    }

}
