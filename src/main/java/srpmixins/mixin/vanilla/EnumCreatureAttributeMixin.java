package srpmixins.mixin.vanilla;

import net.minecraft.entity.EnumCreatureAttribute;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import srpmixins.util.ParasiteCreatureAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(EnumCreatureAttribute.class)
public abstract class EnumCreatureAttributeMixin {
    @Shadow @Final @Mutable private static EnumCreatureAttribute[] $VALUES;
    @Invoker("<init>") private static EnumCreatureAttribute srpmixins_invokeInit(String internalName, int internalId){ throw new AssertionError("SRPMixins couldn't find constructor for EnumCreatureAttribute");}

    static {
        ParasiteCreatureAttribute.PARASITE = srpmixins$addVariant("PARASITE");
    }

    @Unique
    private static EnumCreatureAttribute srpmixins$addVariant(String internalName){
        List<EnumCreatureAttribute> variants = new ArrayList<>(Arrays.asList($VALUES));
        EnumCreatureAttribute newEnumCreatureAttribute = srpmixins_invokeInit(internalName, variants.get(variants.size()-1).ordinal()+1);
        variants.add(newEnumCreatureAttribute);
        $VALUES = variants.toArray(new EnumCreatureAttribute[0]);
        return newEnumCreatureAttribute;
    }
}
