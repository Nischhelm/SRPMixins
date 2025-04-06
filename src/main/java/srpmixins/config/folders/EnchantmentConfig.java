package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class EnchantmentConfig {
    @Config.Comment("Parasite Piercer Enchantment")
    @Config.Name("Parasite Piercer")
    @MixinConfig.SubInstance
    public PiercerConfig piercer = new PiercerConfig();

    @Config.Comment("Parasite Slicer Enchantment")
    @Config.Name("Parasite Slicer")
    @MixinConfig.SubInstance
    public SlicerConfig slicer = new SlicerConfig();

    public static class PiercerConfig{
        @Config.Comment("Register Parasite Piercer Enchantment")
        @Config.Name("Parasite Piercer - Enabled")
        @Config.RequiresMcRestart
        @MixinConfig.LateMixin(name = "mixins.srpmixins.enchantment_piercer.json")
        public boolean enabled = true;

        @Config.Comment("Max Lvl of Parasite Piercer Enchantment")
        @Config.Name("Parasite Piercer - Max Lvl")
        public int maxLvl = 3;

        @Config.Comment("Chance to ignore dmg cap per enchantment lvl of Parasite Piercer Enchantment")
        @Config.Name("Parasite Piercer - Chance to ignore")
        public float ignoreChance = 0.2F;

        @Config.Comment("Whether Parasite Piercer Enchantment is a treasure enchant")
        @Config.Name("Parasite Piercer - Treasure")
        public boolean isTreasure = false;

        @Config.Comment("Whether Parasite Piercer Enchantment should be incompatible with Fire Aspect")
        @Config.Name("Parasite Piercer - Incompatibility")
        public boolean incompatible = true;
    }

    public static class SlicerConfig {
        @Config.Comment("Register Parasite Slicer Enchantment")
        @Config.Name("Parasite Slicer - Enabled")
        @Config.RequiresMcRestart
        @MixinConfig.LateMixin(name = "mixins.srpmixins.enchantment_slicer.json")
        public boolean enabled = true;

        @Config.Comment("Max Lvl of Parasite Slicer Enchantment")
        @Config.Name("Parasite Slicer - Max Lvl")
        public int maxLvl = 5;

        @Config.Comment("Added Damage per enchantment lvl of Parasite Slicer Enchantment")
        @Config.Name("Parasite Slicer - Damage")
        public float dmg = 2.5F;

        @Config.Comment("Whether Parasite Slicer Enchantment is a treasure enchant")
        @Config.Name("Parasite Slicer - Treasure")
        public boolean isTreasure = false;

        @Config.Comment("Whether Parasite Slicer Enchantment should be incompatible with Sharp/Smite/BoA")
        @Config.Name("Parasite Slicer - Incompatibility")
        public boolean incompatible = true;
    }
}
