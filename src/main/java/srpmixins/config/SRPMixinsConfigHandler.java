package srpmixins.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.config.folders.*;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Config(modid = SRPMixins.MODID)
public class SRPMixinsConfigHandler {
	@Config.Comment("Chunk Phases Options - incompatible with player phases")
	@Config.Name("Chunk Phases")
	public static final ChunkPhaseConfig chunkphases = new ChunkPhaseConfig();

	@Config.Comment("Call of the Hive Options")
	@Config.Name("COTH")
	public static final CothConfig coth = new CothConfig();

	@Config.Comment("Damage Fix Options")
	@Config.Name("Damage Fixes")
	public static final DamageFixConfig dmgfix = new DamageFixConfig();

	@Config.Comment("Deterrent and Nexus Options")
	@Config.Name("Deterrents and Nexus")
	public static final DeterrentConfig deterrents = new DeterrentConfig();

	@Config.Comment("Dimension multiplier Options")
	@Config.Name("Dimension Multipliers")
	public static final DimensionConfig dimension = new DimensionConfig();

	@Config.Comment("Lure and Carcass Options")
	@Config.Name("Lures and Carcasses")
	public static final LureConfig lures = new LureConfig();

	@Config.Comment("Compatibility with Lost Cities and Bloodmoon mods")
	@Config.Name("Mod Compats")
	public static final ModCompatConfig modcompat = new ModCompatConfig();

	@Config.Comment("Player Phases Options - incompatible with chunk phases")
	@Config.Name("Player Phases")
	public static final PlayerPhaseConfig playerphases = new PlayerPhaseConfig();

	@Config.Comment("Evolution Phase Point Tweaks and Fixes")
	@Config.Name("Evolution Phase Points")
	public static final PointConfig phasepoints = new PointConfig();

	@Config.Comment("Assimilated and Feral Enderman Options")
	@Config.Name("Assimilated and Feral Endermen")
	public static final SimmermanConfig simmermen = new SimmermanConfig();

	@Config.Comment("Various Options")
	@Config.Name("Various")
	public static final VariousConfig various = new VariousConfig();

	@Config.Comment("Living and Sentient Weapon Options")
	@Config.Name("SRP Weapons")
	public static final WeaponConfig weapons = new WeaponConfig();

	@Mod.EventBusSubscriber(modid = SRPMixins.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(SRPMixins.MODID)) {
				ConfigManager.sync(SRPMixins.MODID, Config.Type.INSTANCE);
				SRPMixinsConfigProvider.reset();
			}
		}
	}

	//Courtesy of fonnymunkey RLMixins
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
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			}
		}
		//If config is not generated or missing entries, don't enable injection on first run
		return configBooleanString.contains("B:\"" + name + "\"=true");
	}
}