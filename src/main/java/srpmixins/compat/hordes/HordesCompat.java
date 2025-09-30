package srpmixins.compat.hordes;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.hordes.config.data.DataRegistry;

public class HordesCompat {
    public static void init() {
        DataRegistry.registerValueGetter(new ResourceLocation("srparasites:phase"), ParasitePhaseGetter::deserialize);
        DataRegistry.registerValueGetter(new ResourceLocation("srparasites:kills"), ParasiteKillGetter::deserialize);
        DataRegistry.registerValueGetter(new ResourceLocation("srparasites:nodes"), ParasiteNodesGetter::deserialize);

        DataRegistry.registerConditionDeserializer(new ResourceLocation("srpmixins:phase"), ParasitePhaseCondition::deserialize);
        DataRegistry.registerConditionDeserializer(new ResourceLocation("srpmixins:kills"), ParasiteKillsCondition::deserialize);
        DataRegistry.registerConditionDeserializer(new ResourceLocation("srpmixins:nodes"), ParasiteNodesCondition::deserialize);
    }
}
