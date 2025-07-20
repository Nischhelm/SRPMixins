package srpmixins.mixin.modcompat.incontrol;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mcjty.incontrol.rules.*;
import mcjty.tools.typed.Attribute;
import mcjty.tools.typed.GenericAttributeMapFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.compat.InControlCompat;

@Mixin(value = {
        ExperienceRule.class,
        SpawnRule.class,
        PotentialSpawnRule.class,
        SummonAidRule.class,
        LootRule.class
})
public abstract class RulesMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lmcjty/tools/typed/GenericAttributeMapFactory;attribute(Lmcjty/tools/typed/Attribute;)Lmcjty/tools/typed/GenericAttributeMapFactory;", ordinal = 0, remap = false)
    )
    private static GenericAttributeMapFactory srpmixins_addCustomRules(GenericAttributeMapFactory original){
        return original
                .attribute(Attribute.create(InControlCompat.MINEVOPHASE))
                .attribute(Attribute.create(InControlCompat.MAXEVOPHASE))
                .attribute(Attribute.create(InControlCompat.MINNODES))
                .attribute(Attribute.create(InControlCompat.MAXNODES))
                .attribute(Attribute.create(InControlCompat.MINCOLOS))
                .attribute(Attribute.create(InControlCompat.MAXCOLOS));
    }
}
