package srpmultiplier.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmultiplier.SRPMultiplier;

import java.util.ArrayList;
import java.util.HashMap;

@Config(modid = SRPMultiplier.MODID)
public class SRPMultiplierConfigHandler {
	
	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

	public static class ServerConfig {

		@Config.Comment("Set to false to fully disable dimension stat+drop multipliers")
		@Config.Name("Parasite Stat+Drop Multiplier: Global switch")
		public boolean doMultipliers = true;

		@Config.Comment("Changes the global stat (dmg, health, armor, kb resistance) multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
		@Config.Name("Parasite Stat Multipliers")
		public String[] dimensionStatMultipliers = {
				"-1,2",
				"0,1",
				"1,2",
				"3,4",
				"111,4"
		};

		@Config.Comment("Decreases drop chance of SRP Items per dimension. Set to 1 for default behavior")
		@Config.Name("Parasite Drop chance Multipliers")
		public String[] dimensionDropMultipliers = {
				"-1,0.5",
				"0,0.25",
				"1,0.5",
				"3,1",
				"111,1"
		};

		@Config.Comment("Disable Lures in LC and instead spawn a Dispatcher Nidus")
		@Config.Name("Lures disabled in LC")
		public boolean disableLuresInLC = true;

		@Config.Comment("Bloody Clock also displays progress to next phase in percent")
		@Config.Name("Bloody Clock percentage")
		public boolean modifyBloodyClock = true;

		@Config.Comment("Play respective sounds when Beckons or Dispatchers of higher stages naturally spawn")
		@Config.Name("Play Sounds")
		public boolean playsounds = true;

		@Config.Comment("Blood moons happen in Lost Cities dimension (requires this mod on client to see red moon), with increased parasite mob cap")
		@Config.Name("Do Blood Moons in LC")
		public boolean bloodmoonInLC = true;

		@Config.Comment("Multiply Parasite Mob Cap by this much during Blood Moons (if using SRP custom spawner)")
		@Config.Name("Bloodmoon Parasite Cap Multiplier")
		public int bloodmoonInLCmobCapMultiplier = 4;

		@Config.Comment("Distance from which Assimilated and Feral Endermen search for mobs to tp, default 64 (performance)")
		@Config.Name("Assimilated/Feral Endermen tp radius")
		public double simmermenTpDistance = 40.0;

		@Config.Comment("LC Portals are locked until reaching this phase. Disable with -1")
		@Config.Name("LC Portal Phase Lock")
		public int portalLClockedPhase = 6;

		@Config.Comment("Custom Mob Cap for Nexus Parasites (Dispatcher+Beckon) using SRP Phase Custom Spawner. Nexus Parasites still count to the global SRP Mob Cap. Disable with -1")
		@Config.Name("Nexus Mob Cap")
		public int nexusCap = 15;

		@Config.Comment("Whitelist Deterrent and Nexus mobs to take dmg per second if world is in low evolution phase")
		@Config.Name("Deterrents take damage from low phase whitelist ")
		public String[] whiteListedDeterrents = {"srparasites:kyphosis","srparasites:sentry","srparasites:seizer","srparasites:dispatcherten","srparasites:beckon_si","srparasites:beckon_sii","srparasites:beckon_siii","srparasites:beckon_siv","srparasites:dispatcher_si","srparasites:dispatcher_sii","srparasites:dispatcher_siii","srparasites:dispatcher_siv"};

		@Config.Comment("Set to true to use Deterrent taking dmg whitelist as blacklist")
		@Config.Name("Deterrent whitelist is blacklist")
		public boolean blackListDeterrents = false;

		@Config.Comment("Only give one penalty of evolution phase points when players sleep instead of a penalty per sleeping player (if player phases off)")
		@Config.Name("Flat sleep point penalty")
		public boolean flatSleepPenalty = true;

		@Config.Comment("Make Assimilated Endermen be able to despawn if they got converted in the end (performance)")
		@Config.Name("End Simmermen despawn")
		public boolean despawnEndSimmermen = true;

		@Config.Comment("Max amount of Assimilated Endermen that can spawn via assimilation in the end (Disable with -1)")
		@Config.Name("End Simmermen Conversion Cap")
		public int endSimmermenCap = 40;

		@Config.Comment("Change Carcass Point Reduction based on Phase")
		@Config.Name("Phase dependent Carcass Values")
		public boolean variableCarcassValues = true;

		@Config.Comment("Phase multiplier on carcass values (0 to 10). Default values are balanced against Carcasses having values of 1,3,10,40,100,1000 for the 6 available Carcass variants in SRPSystems cfg.")
		@Config.Name("Carcass Phase Multipliers")
		public int[] carcassPhaseMultis = {40,40,80,1000,6000,50000,200000,200000,200000,400000,400000};

		@Config.Comment("Do Phase+Point functionalities per player, allowing better Multiplayer")
		@Config.Name("Use Player Phases")
		public boolean playerPhases = true;

		@Config.Comment("Players can only get point penalty from adapted mobs despawning from this phase onwards")
		@Config.Name("Adapted Despawn Penalty First Phase")
		public int adaptedDespawnPenaltyPhase = 4;

		@Config.Comment("Disables the automatic debug logging spam for Scent Entities")
		@Config.Name("Disable Scent Debug")
		public boolean disableScentDebug = true;

		@Config.Comment("Makes Succors deal fixed damage instead of creating entities dmg x2")
		@Config.Name("Fix Succor Damage")
		public boolean fixSuccorDamage = true;

		@Config.Comment("How much damage Succors should deal (x6 in Hard mode with x4 multiplier)")
		@Config.Name("Fix Succor Damage - Dealt damage")
		public float fixedSuccorDamage = 30;

		@Config.Comment("Send logs when methods try to find a player to do player phase stuff with and not finding one")
		@Config.Name("Player Phases debug mode")
		public boolean debugMode = false;

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

		@Config.Comment("Make SRP Blacklists/Whitelists use wildcards to dis/enable whole mods (*)")
		@Config.Name("SRP Blacklists are Wildcard-able")
		public boolean blacklistsWildcardable = true;
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
					SRPMultiplier.LOGGER.warn(SRPMultiplier.NAME + " config could not parse biome blacklist line {}", line);
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
                    SRPMultiplier.LOGGER.warn(SRPMultiplier.NAME + " config could not parse dimension multiplier line {}", line);
				}
			}
		}
	}

	@Mod.EventBusSubscriber(modid = SRPMultiplier.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(SRPMultiplier.MODID)) {
				ConfigManager.sync(SRPMultiplier.MODID, Config.Type.INSTANCE);
			}
		}
	}
}