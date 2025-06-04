package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class DeterrentConfig {
    @Config.Comment("Custom Mob Cap for Nexus Parasites (Dispatcher+Beckon) using SRP Phase Custom Spawner. Nexus Parasites still count to the global SRP Mob Cap. Disable with -1, requires MC restart for full disable")
    @Config.Name("Nexus Mob Cap")
    @Config.RangeInt(min = 0)
    public int nexusCap = 15;

    @Config.Comment("Whitelist Deterrent and Nexus mobs to take dmg per second if world is in low evolution phase")
    @Config.Name("Deterrents take damage from low phase whitelist ")
    public String[] whiteListedDeterrents = {
    };

    @Config.Comment("Set to true to use Deterrent taking dmg whitelist as blacklist")
    @Config.Name("Deterrent whitelist is blacklist")
    public boolean blackListDeterrents = true;

    @Config.Comment("Play respective sounds when Beckons or Dispatchers of higher stages naturally spawn")
    @Config.Name("Play high stage Beckon+Dispatcher spawn sounds")
    @Config.RequiresMcRestart
    public boolean playsounds = true;

    @Config.Comment("Deny Stage 3 Beckons growing up if a Stage 4 Beckon is already nearby (20 blocks distance)")
    @Config.Name("Limit Stage 4 Beckons")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.beckonupgradedeny.json")
    public boolean limitStage4Beckons = true;

    @Config.Comment("Fix beckons reverting all the infested blocks around them on stage increase if evolution is disabled.\n" +
            "This also fixes dying higher stage beckons reverting infested blocks (if evolution is disabled) ignoring the SRP config \"Reinforcement System Block Revert Stage\" value, which would only allow reversion if the infested blocks were made by beckons with stage lower or equal to the config value.")
    @Config.Name("Fix Infested Block Reversion")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.beckoninfestationfix.json")
    public boolean fixInfestedBlockReversion = true;

    @Config.Comment("Beckon infested area grows a lot of infested grass in it. Increase this multiplier to make it more rare (default SRP is 1, default here is 8x rarer).")
    @Config.Name("Infested Grass Rarity")
    public float infestedGrassSpawnRateMultiplier = 8.F;

    @Config.Comment("Beckons infest blocks in a square shaped area. This makes the area circular instead, looks better.")
    @Config.Name("Circular Infestation Area")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.circularinfestationarea.json")
    public boolean infestationAreaIsCircular = true;
}
