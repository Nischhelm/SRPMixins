package srpmixins;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmixins.compat.CompatUtil;
import srpmixins.compat.overlast.OverLastCompat;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMixinsPlugin implements IFMLLoadingPlugin {

	public SRPMixinsPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.scentperformance.json"); //TODO: overhaul scents and make toggles

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.oe.json", () -> Loader.isModLoaded("oe"));

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespawnblacklist.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.deterrentlowphasedmg.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infestedgrassrarity.json");

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.minmaxphasedays.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.conversionpathways.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.variantrules.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.despawntimer.json");

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.blockbreakblacklist.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.foodstealblacklist.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.minferalisations.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermantp.json");

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.mobcapmulti.json", () -> EarlyConfigReader.getBoolean("Parasite Stat+Drop Multiplier: Global switch", SRPMixinsConfigHandler.dimension.doMultipliers) && !EarlyConfigReader.getBoolean("Fix Spawning Entirely", SRPMixinsConfigHandler.spawns.fixSpawningEntirely));

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adapteddespawnlock.json", () -> EarlyConfigReader.getInt("Adapted Despawn Penalty First Phase", SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadpenalty.json", () -> EarlyConfigReader.getInt("Biome Spreading Penalty First Phase", SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase) > -1 && !EarlyConfigReader.getBoolean("Biome Spread Performance Overhaul", SRPMixinsConfigHandler.parabiome.biomeSpreadOverhaul));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infestationpenalty.json", () -> EarlyConfigReader.getInt("Infestation Penalty First Phase", SRPMixinsConfigHandler.phasepoints.infestationPenaltyPhase) > -1 && !EarlyConfigReader.getBoolean("Infestation Performance Overhaul", SRPMixinsConfigHandler.deterrents.infestationOverhaul));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infsquidcap.json", () -> EarlyConfigReader.getInt("Water Parasite Mob Cap", SRPMixinsConfigHandler.waterparas.waterParasiteCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nexuscap.json", () -> EarlyConfigReader.getInt("Nexus Mob Cap", SRPMixinsConfigHandler.deterrents.nexusCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nodecolonyreset.json", () -> EarlyConfigReader.getInt("Node/Colony Removal Check Frequency", SRPMixinsConfigHandler.parabiome.nodeColonyResetCheckFrequency) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simbigspiderassimfix.json", () -> EarlyConfigReader.getInt("Sim Big Spider Min Assimilations", SRPMixinsConfigHandler.coth.assimBigSpiderMinAssimilations) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermancap.json", () -> EarlyConfigReader.getInt("End Simmermen Conversion Cap", SRPMixinsConfigHandler.simmermen.endSimmermenCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.spawninglight.json", () -> EarlyConfigReader.getInt("Min Blocklight Threshold", SRPMixinsConfigHandler.spawns.blockLightThresholdTwo) != 7);

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlast.json", () -> Loader.isModLoaded("overlast"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlast_customphases.json", () -> OverLastCompat.shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion.FULL));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlastlite_customphases.json", () -> OverLastCompat.shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion.LITE));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.deepseadanger_customphases.json", () -> Loader.isModLoaded("srpdeepseadanger") && CompatUtil.areCustomPhasesEnabled());
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srpextra_customphases.json", () -> Loader.isModLoaded("srpextra") && CompatUtil.areCustomPhasesEnabled());

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.bloodmoon.entirespawnfix.json", () -> Loader.isModLoaded("bloodmoon") && EarlyConfigReader.getBoolean("Fix Spawning Entirely", SRPMixinsConfigHandler.spawns.fixSpawningEntirely));

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.beckoninfestationlimit.json", () -> EarlyConfigReader.getBoolean("Fix Block Infestation Limit", SRPMixinsConfigHandler.deterrents.fixInfestedBlockLimit) && !EarlyConfigReader.getBoolean("Infestation Performance Overhaul", SRPMixinsConfigHandler.deterrents.infestationOverhaul));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.circularinfestationarea.json", () -> EarlyConfigReader.getBoolean("Circular Infestation Area", SRPMixinsConfigHandler.deterrents.infestationAreaIsCircular) && !EarlyConfigReader.getBoolean("Infestation Performance Overhaul", SRPMixinsConfigHandler.deterrents.infestationOverhaul));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.customphasesnoinfestationoverhaul.json", () -> (EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled) || EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled)) && !EarlyConfigReader.getBoolean("Infestation Performance Overhaul", SRPMixinsConfigHandler.deterrents.infestationOverhaul));

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadlimit.json", () -> EarlyConfigReader.getBoolean("Fix Parasitic Biome spreading limit", SRPMixinsConfigHandler.parabiome.fixBiomeSpreadingLimit) && !EarlyConfigReader.getBoolean("Biome Spread Performance Overhaul", SRPMixinsConfigHandler.parabiome.biomeSpreadOverhaul));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.customphasesnobiomespreadoverhaul.json", () -> (EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled) || EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled)) && !EarlyConfigReader.getBoolean("Biome Spread Performance Overhaul", SRPMixinsConfigHandler.parabiome.biomeSpreadOverhaul));
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
