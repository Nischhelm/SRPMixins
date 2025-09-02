package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class LootConfig {
    @Config.Comment("Main toggle for the whole functionality in this section. \n" +
            "Makes all SRP drop loot tables use vanilla loot table jsons that will be written into config/srpmixins/loot_tables/ and can be modified there.\n" +
            "Delete files there to make them regenerate from SRPs config system.")
    @Config.Name("Use Vanilla Loot Tables")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.loottables.json", defaultValue = true)
    public boolean useLootTables = true;
}
