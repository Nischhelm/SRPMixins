package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class DeterrentConfig {
    @Config.Comment("Custom Mob Cap for Nexus Parasites (Dispatcher+Beckon) using SRP Phase Custom Spawner. Nexus Parasites still count to the global SRP Mob Cap. Disable with -1, requires MC restart for full disable")
    @Config.Name("Nexus Mob Cap")
    @Config.RangeInt(min = -1)
    public int nexusCap = 15;

    @Config.Comment("Whitelist Deterrent and Nexus mobs to take dmg per second if world is in low evolution phase")
    @Config.Name("Deterrents take damage from low phase whitelist ")
    public String[] whiteListedDeterrents = {
            "srparasites:kyphosis",
            "srparasites:sentry",
            "srparasites:seizer",
            "srparasites:dispatcherten",
            "srparasites:beckon_si",
            "srparasites:beckon_sii",
            "srparasites:beckon_siii",
            "srparasites:beckon_siv",
            "srparasites:dispatcher_si",
            "srparasites:dispatcher_sii",
            "srparasites:dispatcher_siii",
            "srparasites:dispatcher_siv"
    };

    @Config.Comment("Set to true to use Deterrent taking dmg whitelist as blacklist")
    @Config.Name("Deterrent whitelist is blacklist")
    public boolean blackListDeterrents = false;

    @Config.Comment("Play respective sounds when Beckons or Dispatchers of higher stages naturally spawn")
    @Config.Name("Play high stage Beckon+Dispatcher spawn sounds")
    @Config.RequiresMcRestart
    public boolean playsounds = true;

    @Config.Comment("Deny Stage 3 Beckons growing up if a Stage 4 Beckon is already nearby (20 blocks distance)")
    @Config.Name("Limit Stage 4 Beckons")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.beckonupgradedeny.json", defaultValue = true)
    public boolean limitStage4Beckons = true;

    @Config.Comment("Fix beckons reverting all the infested blocks around them on stage increase if evolution is disabled.\n" +
            "This also fixes dying higher stage beckons reverting infested blocks (if evolution is disabled) ignoring the SRP config \"Reinforcement System Block Revert Stage\" value, which would only allow reversion if the infested blocks were made by beckons with stage lower or equal to the config value.")
    @Config.Name("Fix Infested Block Reversion")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.beckoninfestationfix.json", defaultValue = true)
    public boolean fixInfestedBlockReversion = true;

    @Config.Comment("SRP only uses the Beckon Stage III Infestation Limits (in SRParasitesMobs.cfg) instead of limiting the infestation spread by beckon stage, essentially ignoring the stage I and stage II config values. This fixes it.")
    @Config.Name("Fix Block Infestation Limit")
    public boolean fixInfestedBlockLimit = true;

    @Config.Comment("When Beckons stand inside infested grass, they will not be able to propagate their new stage (after upgrade) to the infested blocks around them, making some infestation mechanics not work properly in those cases. This fixes it.")
    @Config.Name("Fix Block Infestation Beckon Upgrade")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.aiblockinfestfix.json", defaultValue = true)
    public boolean fixKyphosisInfestation = true;

    @Config.Comment("Beckon infested area grows a lot of infested grass in it. Increase this multiplier to make it more rare (default SRP is 1, default here is 8x rarer).")
    @Config.Name("Infested Grass Rarity")
    @Config.RangeDouble(min = 0F)
    public float infestedGrassSpawnRateMultiplier = 8F;

    @Config.Comment("Beckons infest blocks in a square shaped area. This makes the area circular instead, looks better.")
    @Config.Name("Circular Infestation Area")
    public boolean infestationAreaIsCircular = true;

    @Config.Comment("If set to true, fully disables the infestation reversion mechanic where killing a Beckon above certain infested blocks with low enough internal \"stage\" they will turn back to various non-infested blocks.")
    @Config.Name("Disable Infestation Reversion")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.infestationreversiontoggle.json", defaultValue = false)
    public boolean infestationReversionToggle = false;

    @Config.Comment("Fully overhauls the beckon infestation spread logic to make it more performant. If there are other mods that use mixins with the infestation system, there will be incompatibilities.\n" +
            "Since there are also specific mixins into that logic from SRPMixins itself, you'll have to disable those.\n" +
            "\t- Circular Infestation Area\n" +
            "\t- Fix Block Infestation Limit\n" +
            "Incompatible with Cotesia Glomerata")
    @Config.Name("Infestation Performance Overhaul")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.infestationoverhaul.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "srpcotesia", desired = false, reason = "Disable \"Infestation Performance Overhaul\"!", warnIngame = false)
    public boolean infestationOverhaul = true;

    @Config.Comment("Will allow quenches to turn remain blocks to air on impact.")
    @Config.Name("Quenchs Remove Remains")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.quenchremovesremains.json", defaultValue = true)
    public boolean quenchRemovesRemains = true;
}
