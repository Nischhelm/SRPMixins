package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.config.folders.*;

@Config(modid = SRPMixins.MODID)
public class SRPMixinsConfigHandler {
	@Config.Comment("Adaptation Options")
	@Config.Name("Adaptation")
	@MixinConfig.SubInstance
	public static final AdaptationConfig adaptation = new AdaptationConfig();

	@Config.Comment("Chunk Phases Options - incompatible with player phases")
	@Config.Name("Chunk Phases")
	@MixinConfig.SubInstance
	public static final ChunkPhaseConfig chunkphases = new ChunkPhaseConfig();

	@Config.Comment("Call of the Hive Options")
	@Config.Name("COTH")
	@MixinConfig.SubInstance
	public static final CothConfig coth = new CothConfig();

	@Config.Comment("Damage Fix Options")
	@Config.Name("Damage Fixes")
	@MixinConfig.SubInstance
	public static final DamageFixConfig dmgfix = new DamageFixConfig();

	@Config.Comment("Deterrent and Nexus Options")
	@Config.Name("Deterrents and Nexus")
	@MixinConfig.SubInstance
	public static final DeterrentConfig deterrents = new DeterrentConfig();

	@Config.Comment("Dimension multiplier Options")
	@Config.Name("Dimension Multipliers")
	@MixinConfig.SubInstance
	public static final DimensionConfig dimension = new DimensionConfig();

	@Config.Comment("Enchantment Options")
	@Config.Name("Enchantments")
	@MixinConfig.SubInstance
	public static final EnchantmentConfig enchantments = new EnchantmentConfig();

	@Config.Comment("Lure and Carcass Options")
	@Config.Name("Lures and Carcasses")
	@MixinConfig.SubInstance
	public static final LureConfig lures = new LureConfig();

	@Config.Comment("Compatibility with Lost Cities and Bloodmoon mods")
	@Config.Name("Mod Compats")
	@MixinConfig.SubInstance
	public static final ModCompatConfig modcompat = new ModCompatConfig();

	@Config.Comment("SRP phase configs for quick access and increase/decrease of phases")
	@Config.Name("More Phases")
	@MixinConfig.SubInstance
	public static final MorePhasesConfig morephases = new MorePhasesConfig();

	@Config.Comment("Player Phases Options - incompatible with chunk phases")
	@Config.Name("Player Phases")
	@MixinConfig.SubInstance
	public static final PlayerPhaseConfig playerphases = new PlayerPhaseConfig();

	@Config.Comment("Evolution Phase Point Tweaks and Fixes")
	@Config.Name("Evolution Phase Points")
	@MixinConfig.SubInstance
	public static final PointConfig phasepoints = new PointConfig();

	@Config.Comment("Potion Tweaks and Fixes")
	@Config.Name("Potions")
	@MixinConfig.SubInstance
	public static final PotionConfig potions = new PotionConfig();

	@Config.Comment("Assimilated and Feral Enderman Options")
	@Config.Name("Assimilated and Feral Endermen")
	@MixinConfig.SubInstance
	public static final SimmermanConfig simmermen = new SimmermanConfig();

	@Config.Comment("Spawn Fixes and Tweaks")
	@Config.Name("Spawning")
	@MixinConfig.SubInstance
	public static final SpawnConfig spawns = new SpawnConfig();

	@Config.Comment("Quick Access for common SRP mob configs like stat multis")
	@Config.Name("SRP Mob Config")
	@MixinConfig.SubInstance
	public static final SRPMobConfig mobConfig = new SRPMobConfig();

	@Config.Comment("Various Options")
	@Config.Name("Various")
	@MixinConfig.SubInstance
	public static final VariousConfig various = new VariousConfig();

	@Config.Comment("Options for Assimilated Squids and Primitive Devourers")
	@Config.Name("Water Parasites")
	@MixinConfig.SubInstance
	public static final WaterParaConfig waterparas = new WaterParaConfig();

	@Config.Comment("Living and Sentient Weapon Options")
	@Config.Name("SRP Weapons")
	@MixinConfig.SubInstance
	public static final WeaponConfig weapons = new WeaponConfig();

	@Mod.EventBusSubscriber(modid = SRPMixins.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(SRPMixins.MODID)) {
				ConfigManager.sync(SRPMixins.MODID, Config.Type.INSTANCE);
				SRPMixinsConfigProvider.reset();
				if(SRPMixinsConfigHandler.mobConfig.enableMobConfig) {
					SRPAttributes.reset(); //TODO: does this even work with early loading
					SRPAttributes.init();
				}
				SRPMixinsConfigProvider.readConversionLockConfig();
			}
		}
	}
}