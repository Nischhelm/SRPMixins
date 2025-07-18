package srpmixins.mixin.vanilla;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.IAnimals;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import srpmixins.util.IParasite;
import srpmixins.util.ParasiteCreatureType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(EnumCreatureType.class)
public abstract class EnumCreatureTypeMixin {
    @SuppressWarnings("target")
    @Shadow @Final @Mutable private static EnumCreatureType[] $VALUES;
    @Invoker("<init>") public static EnumCreatureType srpmixins_invokeInit(String internalName, int internalId, Class <? extends IAnimals> creatureClassIn, int maxNumberOfCreatureIn, Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn){
        throw new AssertionError("SRPMixins couldn't find constructor for EnumCreatureType");
    }

    static {
        ParasiteCreatureType.PARASITE = srpmixins$addParasiteCreatureAttribute();
    }

    @Unique
    private static EnumCreatureType srpmixins$addParasiteCreatureAttribute(){
        List<EnumCreatureType> variants = new ArrayList<>(Arrays.asList($VALUES));
        //Can't set mob cap to SRPConfig value here, gotta do in WorldEntitySpawnerMixin
        EnumCreatureType newEnumCreatureAttribute = srpmixins_invokeInit("PARASITE", variants.get(variants.size()-1).ordinal()+1, IParasite.class, ParasiteCreatureType.paraMobCapToggle, Material.AIR, false, false);
        variants.add(newEnumCreatureAttribute);
        $VALUES = variants.toArray(new EnumCreatureType[0]);
        return newEnumCreatureAttribute;
    }
}
