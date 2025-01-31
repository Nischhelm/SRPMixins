package srpmixins.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import srpmixins.SRPMixins;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Config(modid = SRPMixins.MODID)
public class SRPMixinsConfigHandler {
	
	@Config.Comment("Various Options")
	@Config.Name("Various")
	public static final VariousConfig various = new VariousConfig();

	@Config.Comment("Damage Fix Options")
	@Config.Name("Damage Fixes")
	public static final DamageFixConfig dmgfix = new DamageFixConfig();

	@Config.Comment("Compatibility with Lost Cities and Bloodmoon mods")
	@Config.Name("Mod Compats")
	public static final ModCompatConfig modcompat = new ModCompatConfig();

	@Config.Comment("Dimension multiplier Options")
	@Config.Name("Dimension Multipliers")
	public static final DimensionConfig dimension = new DimensionConfig();

	@Config.Comment("Lure and Carcass Options")
	@Config.Name("Lures and Carcasses")
	public static final LureConfig lures = new LureConfig();

	@Config.Comment("Living and Sentient Weapon Options")
	@Config.Name("SRP Weapons")
	public static final WeaponConfig weapons = new WeaponConfig();

	@Config.Comment("Evolution Phase Point Tweaks and Fixes")
	@Config.Name("Evolution Phase Points")
	public static final PointConfig phasepoints = new PointConfig();

	@Config.Comment("Deterrent and Nexus Options")
	@Config.Name("Deterrents and Nexus")
	public static final DeterrentConfig deterrents = new DeterrentConfig();

	@Config.Comment("Assimilated and Feral Enderman Options")
	@Config.Name("Assimilated and Feral Endermen")
	public static final SimmermanConfig simmermen = new SimmermanConfig();

	@Config.Comment("Call of the Hive Options")
	@Config.Name("COTH")
	public static final CothConfig coth = new CothConfig();

	public static class DeterrentConfig {
		@Config.Comment("Custom Mob Cap for Nexus Parasites (Dispatcher+Beckon) using SRP Phase Custom Spawner. Nexus Parasites still count to the global SRP Mob Cap. Disable with -1")
		@Config.Name("Nexus Mob Cap")
		public int nexusCap = 15;

		@Config.Comment("Whitelist Deterrent and Nexus mobs to take dmg per second if world is in low evolution phase")
		@Config.Name("Deterrents take damage from low phase whitelist ")
		public String[] whiteListedDeterrents = {"srparasites:kyphosis", "srparasites:sentry", "srparasites:seizer", "srparasites:dispatcherten", "srparasites:beckon_si", "srparasites:beckon_sii", "srparasites:beckon_siii", "srparasites:beckon_siv", "srparasites:dispatcher_si", "srparasites:dispatcher_sii", "srparasites:dispatcher_siii", "srparasites:dispatcher_siv"};

		@Config.Comment("Set to true to use Deterrent taking dmg whitelist as blacklist")
		@Config.Name("Deterrent whitelist is blacklist")
		public boolean blackListDeterrents = false;

		@Config.Comment("Play respective sounds when Beckons or Dispatchers of higher stages naturally spawn")
		@Config.Name("Play high stage Beckon+Dispatcher spawn sounds")
		public boolean playsounds = true;

		@Config.Comment("Deny Stage 3 Beckons growing up if a Stage 4 Beckon is already nearby (20 blocks distance)")
		@Config.Name("Limit Stage 4 Beckons")
		public boolean limitStage4Beckons = true;
	}

	public static class PointConfig {
		@Config.Comment("Bloody Clock also displays progress to next phase in percent")
		@Config.Name("Bloody Clock percentage")
		public boolean modifyBloodyClock = true;

		@Config.Comment("If Bloody CLock percentage is true, also show point cooldown when using the clock")
		@Config.Name("Bloody Clock shows cooldown")
		public boolean bloodyClockShowsCooldown = true;

		@Config.Comment("Only give one penalty of evolution phase points when players sleep instead of a penalty per sleeping player (if player phases off)")
		@Config.Name("Flat sleep point penalty")
		public boolean flatSleepPenalty = true;

		@Config.Comment("Do Phase+Point functionalities per player, allowing better Multiplayer")
		@Config.Name("Use Player Phases")
		public boolean playerPhases = true;

		@Config.Comment("Players can only get point penalty from adapted mobs despawning from this phase onwards (disable with -1)")
		@Config.Name("Adapted Despawn Penalty First Phase")
		public int adaptedDespawnPenaltyPhase = 4;

		@Config.Comment("Players can only get point penalty from parasitic biome spreading (disable with -1)")
		@Config.Name("Biome Spreading Penalty First Phase")
		public byte biomeSpreadingPenaltyPhase = 5;

		@Config.Comment("Send logs when methods try to find a player to do player phase stuff with and not finding one")
		@Config.Name("Player Phases debug mode")
		public boolean debugMode = false;

		@Config.Comment("Limit point reduction from parasite kills to the min point value for each phase, stopping unintended phase decreases")
		@Config.Name("Fix phase point reduction")
		public boolean limitPointReduction = true;
	}

	public static class WeaponConfig {
		@Config.Comment("Fully disable the sentient evolution mechanic where living weapons/armor/bow evolve to sentient after x kills")
		@Config.Name("Disable Sentient Evolution Mechanic")
		public boolean disableSentientEvolution = false;

		@Config.Comment("Fix parasites getting hit by sentient weapons not doing the correct things")
		@Config.Name("Fix parasite weapon damage")
		public boolean fixParasiteDmg = true;

		@Config.Comment("Make living weapons evolving to sentient keep their NBT")
		@Config.Name("Fix parasite weapon evolution NBT loss")
		public boolean fixSentientEvolutionNBT = true;

		@Config.Comment("Sentient weapons keep counting parasite kills(/HP) even though it doesn't do anything for them. Set to true to remove this Tooltip")
		@Config.Name("Remove Parasite Kills tooltip from sentient weapons")
		public boolean removeSentientSRPKillsTooltip = true;

		@Config.Comment("Copy the same sentient evolution handling of living weapons to living armor and living bow")
		@Config.Name("Sentient Armor+Bow Evolution")
		public boolean addArmorBowEvolution = true;
    }

	public static class LureConfig {
		@Config.Comment("Change Carcass Point Reduction based on Phase")
		@Config.Name("Phase dependent Carcass Values")
		public boolean variableCarcassValues = true;

		@Config.Comment("Phase multiplier on carcass values (0 to 10). Default values are balanced against Carcasses having values of 1,3,10,40,100,1000 for the 6 available Carcass variants in SRPSystems cfg.")
		@Config.Name("Carcass Phase Multipliers")
		public int[] carcassPhaseMultis = {40, 40, 80, 1000, 6000, 50000, 200000, 200000, 200000, 400000, 400000};

		@Config.Comment("Make Carcasses reduce points while cooldown is active")
		@Config.Name("Fix Carcasses not working during cooldown")
		public boolean fixCarcassDuringCooldown = true;

		@Config.Comment("Make using Lures add their cooldown to current cooldown instead of setting it to a fixed value, possibly even reducing the cooldown by doing that")
		@Config.Name("Lures stack cooldown")
		public boolean lureCooldownStacking = true;

		@Config.Comment("Only allow carcasses built from 5 lures of the same type. Without this fix, only the level of the center lure matters")
		@Config.Name("Force carcass all same lure type")
		public boolean forceCarcassSameLureVariant = true;

		@Config.Comment("When using faint lures, SRP also calls setCooldown for lures 9 and 10 (they forgot to set a break;). To fix this, we just set them to 0 here (can also be fixed by setting them to 0 in SRP configs)")
		@Config.Name("Overwrite lure cooldowns nine and ten with 0")
		public boolean fixCooldownOverflow = true;
	}

	public static class DimensionConfig {
		@Config.Comment("Set to false to fully disable dimension stat+drop+mobcap multipliers")
		@Config.Name("Parasite Stat+Drop Multiplier: Global switch")
		public boolean doMultipliers = true;

		@Config.RequiresMcRestart
		@Config.Comment("Changes the global health multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
		@Config.Name("Parasite Health Multipliers")
		public String[] dimensionHealthMultipliers = {
				"-1,2",
				"0,1",
				"1,2",
				"3,4",
				"111,4"
		};

		@Config.RequiresMcRestart
		@Config.Comment("Changes the global damage multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
		@Config.Name("Parasite Dmg Multipliers")
		public String[] dimensionDmgMultipliers = {
				"-1,2",
				"0,1",
				"1,2",
				"3,4",
				"111,4"
		};

		@Config.RequiresMcRestart
		@Config.Comment("Changes the global armor multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
		@Config.Name("Parasite Armor Multipliers")
		public String[] dimensionArmorMultipliers = {
				"-1,2",
				"0,1",
				"1,2",
				"3,4",
				"111,4"
		};

		@Config.RequiresMcRestart
		@Config.Comment("Changes the global stat knockback resistance multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
		@Config.Name("Parasite KBRes Multipliers")
		public String[] dimensionKBResMultipliers = {
				"-1,2",
				"0,1",
				"1,2",
				"3,4",
				"111,4"
		};

		@Config.RequiresMcRestart
		@Config.Comment("Decreases drop chance of SRP Items per dimension. Set to 1 for default behavior")
		@Config.Name("Parasite Drop chance Multipliers")
		public String[] dimensionDropMultipliers = {
				"-1,0.5",
				"0,0.25",
				"1,0.5",
				"3,1",
				"111,1"
		};

		@Config.RequiresMcRestart
		@Config.Comment("Increases parasite mob cap and per player cap by this multiplier per dimension")
		@Config.Name("Parasite mob cap Multipliers")
		public String[] dimensionMobCapMultipliers = {
				"-1,1",
				"0,1",
				"1,1",
				"3,4",
				"111,4"
		};
	}

	public static class ModCompatConfig {
		@Config.RequiresMcRestart
		@Config.Comment("Enable BloodMoon tweaks (don't set this to true if your modpack doesn't have BloodMoon, otherwise it will crash)")
		@Config.Name("Compat: Modpack has Bloodmoon mod")
		public boolean hasBloodmoon = false;

		@Config.RequiresMcRestart
		@Config.Comment("Enable LostCities tweaks (don't set this to true if your modpack doesn't have LostCities, otherwise it will crash)")
		@Config.Name("Compat: Modpack has LostCities mod")
		public boolean hasLostCities = false;

		@Config.Comment("Disable Lures in LC and instead spawn a Dispatcher Nidus")
		@Config.Name("Lures disabled in LC")
		public boolean disableLuresInLC = true;

		@Config.Comment("Blood moons happen in Lost Cities dimension (requires this mod on client to see red moon), with increased parasite mob cap")
		@Config.Name("Do Blood Moons in LC")
		public boolean bloodmoonInLC = true;

		@Config.Comment("Multiply Parasite Mob Cap by this much during Blood Moons (if using SRP custom spawner)")
		@Config.Name("Bloodmoon Parasite Cap Multiplier")
		public float bloodmoonInLCmobCapMultiplier = 4;

		@Config.Comment("LC Portals are locked until reaching this phase. Disable with -1")
		@Config.Name("LC Portal Phase Lock")
		public int portalLClockedPhase = 6;
	}

	public static class DamageFixConfig {
		@Config.Comment("Set to false to disable all fixes for parasite damages")
		@Config.Name("Damage Fix: Global switch")
		public boolean doDamageFixes = true;

		@Config.Comment("Ancient Overlord homing missile base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
		@Config.Name("Damage Fix: Overlord projectile base damage")
		public float overlordProjectileDamage = 50;

		@Config.Comment("Haunter homing missile base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
		@Config.Name("Damage Fix: Haunter projectile base damage")
		public float haunterProjectileDamage = 32;

		@Config.Comment("Ancient Dreadnaught melee aura base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
		@Config.Name("Damage Fix: Dreadnaught melee aura base damage")
		public float dreadnaughtMeleeDamage = 32;

		@Config.Comment("Bogle melee aura base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
		@Config.Name("Damage Fix: Bogle melee aura base damage")
		public float bogleMeleeDamage = 25;

		@Config.Comment("Wraith melee aura base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
		@Config.Name("Damage Fix: Wraith melee aura base damage")
		public float wraithMeleeDamage = 25;

		@Config.Comment("Makes Succors deal fixed damage instead of 2 times its creator's dmg")
		@Config.Name("Fix Succor Damage")
		public boolean fixSuccorDamage = true;

		@Config.Comment("How much damage Succors should deal (x6 in Hard mode with x4 multiplier)")
		@Config.Name("Fix Succor Damage - Dealt damage")
		public float fixedSuccorDamage = 30;
	}

	public static class SimmermanConfig {
		@Config.Comment("Distance from which Assimilated and Feral Endermen search for mobs to tp, default 64 (performance)")
		@Config.Name("Assimilated/Feral Endermen tp radius")
		public double simmermenTpDistance = 40.0;

		@Config.Comment("Make Assimilated Endermen be able to despawn if they got converted in the end (performance)")
		@Config.Name("End Simmermen despawn")
		public boolean despawnEndSimmermen = true;

		@Config.Comment("Max amount of Assimilated Endermen that can spawn via assimilation in the end (Disable with -1)")
		@Config.Name("End Simmermen Conversion Cap")
		public int endSimmermenCap = 40;
	}

	public static class VariousConfig {
		@Config.Comment("Disables the automatic debug log spam for Scent Entities")
		@Config.Name("Disable Scent Debug")
		public boolean disableScentDebug = true;

		@Config.RequiresMcRestart
		@Config.Comment("Blacklist of biomes and dimensions in which no parasites will spawn. Pattern: dimension id, biome registry name. Disable full mods by dimid, modid. Disable full dimensions by only naming dimid, no biomes for that dimension in any line")
		@Config.Name("Parasite Spawning Biome Blacklist per dimension")
		public String[] biomeBlacklist = {
				"0, minecraft:mutated_forest",
				"3, otg",
				"271"
		};

		@Config.Comment("Use Biome Blacklist as Whitelist")
		@Config.Name("Parasite Spawning Biome Blacklist per dimension is whitelist")
		public boolean biomeBlacklistIsWhitelist = false;

		@Config.Comment("Make SRP Blacklists/Whitelists use wildcards to dis/enable whole mods (*). WARNING: this forces you to change all current SRP config lists that use full mod names without wildcards")
		@Config.Name("SRP Blacklists are Wildcard-able")
		public boolean blacklistsWildcardable = false;
    }

	public static class CothConfig {
		@Config.Comment("Makes mobs getting converted to their assimilated version respect coth immunity")
		@Config.Name("Stop assimilating COTH immune mobs")
		public boolean stopCothImmuneAssim = true;

		@Config.Comment("Makes mobs getting converted to their feral version respect coth immunity")
		@Config.Name("Stop feralizing COTH immune mobs")
		public boolean stopCothImmuneFeral = true;

		@Config.Comment("Fixes the srpcothimmunity tag (basically counting coth lvls) getting incremented for coth immune mobs, making them not immune anymore")
		@Config.Name("Fix srpcothimmunity tag")
		public boolean fixSrpCothImmunity = true;
	}

	public static void setupBiomeBlacklistMap(HashMap<Integer, ArrayList<String>> map, String[] config) {
		for (String line : config) {
			String[] split = line.split(" *, *");
			if (split.length >= 1) {
				try {
					int dim = Integer.parseInt(split[0]);
					if (!map.containsKey(dim))
						map.put(dim, new ArrayList<>());
					if(split.length>=2) {
						String biome = split[1];
						map.get(dim).add(biome);
					}
				} catch (NumberFormatException e) {
					SRPMixins.LOGGER.warn(SRPMixins.NAME + " config could not parse biome blacklist line {}", line);
				}
			}
		}
	}

	public static void setupDimensionMultiplierMap(HashMap<Integer,Float> map, String[] config) {
		for (String line : config) {
			String[] split = line.split(" *, *");
			if (split.length >= 2) {
				try {
					int dim = Integer.parseInt(split[0]);
					float multi = Float.parseFloat(split[1]);
					if (!map.containsKey(dim))
						map.put(dim, multi);
				} catch (NumberFormatException e) {
                    SRPMixins.LOGGER.warn(SRPMixins.NAME + " config could not parse dimension multiplier line {}", line);
				}
			}
		}
	}

	@Mod.EventBusSubscriber(modid = SRPMixins.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(SRPMixins.MODID)) {
				ConfigManager.sync(SRPMixins.MODID, Config.Type.INSTANCE);
			}
		}
	}

	//Courtesy of FonnyMunkey RLMixins
	private static File configFile = null;
	private static String configBooleanString = "";

	public static boolean getBoolean(String name) {
		if(configFile==null) {
			configFile = new File("config", SRPMixins.MODID + ".cfg");
			if(configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					configBooleanString = stream.filter(s -> s.trim().startsWith("B:")).collect(Collectors.joining());
				}
				catch(Exception ex) {
					SRPMixins.LOGGER.log(Level.ERROR, "Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			}
		}
		//If config is not generated or missing entries, don't enable injection on first run
		return configBooleanString.contains("B:\"" + name + "\"=true");
	}
}