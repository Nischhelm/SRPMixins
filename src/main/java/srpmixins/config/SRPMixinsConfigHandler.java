package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.config.folders.*;
import srpmixins.config.providers.ChunkPhaseConfigProvider;
import srpmixins.config.providers.DimensionMultiConfigProvider;
import srpmixins.config.providers.MorePhasesConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.rules.ConversionPathways;
import srpmixins.rules.ruleset.*;

@Config(modid = SRPMixins.MODID)
public class SRPMixinsConfigHandler {
	@Config.Comment("Adaptation Options")
	@Config.Name("Adaptation")
	public static final AdaptationConfig adaptation = new AdaptationConfig();

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

	@Config.Comment("Enchantment Options")
	@Config.Name("Enchantments")
	public static final EnchantmentConfig enchantments = new EnchantmentConfig();

	@Config.Comment("Lure and Carcass Options")
	@Config.Name("Lures and Carcasses")
	public static final LureConfig lures = new LureConfig();

	@Config.Comment("Compatibility with Lost Cities and Bloodmoon mods")
	@Config.Name("Mod Compats")
	public static final ModCompatConfig modcompat = new ModCompatConfig();

	@Config.Comment("SRP phase configs for quick access and increase/decrease of phases")
	@Config.Name("More Phases")
	public static final MorePhasesConfig morephases = new MorePhasesConfig();

	@Config.Comment("Parasite Biome Options")
	@Config.Name("Parasite Biome")
	public static ParaBiomeConfig parabiome = new ParaBiomeConfig();

	@Config.Comment("Player Phases Options - incompatible with chunk phases")
	@Config.Name("Player Phases")
	public static final PlayerPhaseConfig playerphases = new PlayerPhaseConfig();

	@Config.Comment("Evolution Phase Point Tweaks and Fixes")
	@Config.Name("Evolution Phase Points")
	public static final PointConfig phasepoints = new PointConfig();

	@Config.Comment("Potion Tweaks and Fixes")
	@Config.Name("Potions")
	public static final PotionConfig potions = new PotionConfig();

	@Config.Comment("Rule Options")
	@Config.Name("Rules")
	public static final RuleConfig rules = new RuleConfig();

	@Config.Comment("Assimilated and Feral Enderman Options")
	@Config.Name("Assimilated and Feral Endermen")
	public static final SimmermanConfig simmermen = new SimmermanConfig();

	@Config.Comment("Spawn Fixes and Tweaks")
	@Config.Name("Spawning")
	public static final SpawnConfig spawns = new SpawnConfig();

	@Config.Comment("Quick Access for common SRP mob configs like stat multis")
	@Config.Name("SRP Mob Config")
	public static final SRPMobConfig mobConfig = new SRPMobConfig();

	@Config.Comment("Various Options")
	@Config.Name("Various")
	public static final VariousConfig various = new VariousConfig();

	@Config.Comment("Options for Assimilated Squids and Primitive Devourers")
	@Config.Name("Water Parasites")
	public static final WaterParaConfig waterparas = new WaterParaConfig();

	@Config.Comment("Living and Sentient Weapon Options")
	@Config.Name("SRP Weapons")
	public static final WeaponConfig weapons = new WeaponConfig();

	@Config.Comment("Source Dimension Options")
	@Config.Name("Source Dim")
	public static final SourceDimConfig sourcedim = new SourceDimConfig();

    @Mod.EventBusSubscriber(modid = SRPMixins.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(SRPMixins.MODID)) {
				ConfigManager.sync(SRPMixins.MODID, Config.Type.INSTANCE);

				SRPMixinsConfigProvider.reset();
				MorePhasesConfigProvider.reset();
				ChunkPhaseConfigProvider.reset();
				DimensionMultiConfigProvider.reset();

				MobCapRuleSet.INSTANCE.reset();
				MinMaxDayPerPhaseRuleSet.INSTANCE.reset();
				VariantDisableRuleSet.INSTANCE.reset();
				DespawnTimerRuleSet.INSTANCE.reset();
				StatIncreaseRuleSet.INSTANCE.reset();
				ConversionPathways.reset();

				if(SRPMixinsConfigHandler.mobConfig.enableMobConfig && SRPMixinsConfigHandler.mobConfig.mobConfig.length > 0) {
					SRPMobConfigProvider.reset();
					SRPAttributes.reset();
					SRPAttributes.init(); // runs SRPMobConfigProvider.init() via mixin
				}
			}
		}
	}
}