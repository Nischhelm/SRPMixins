package srpmixins;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.compat.overlast.OverLastCompat;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMixinsPlugin implements IFMLLoadingPlugin {

	public SRPMixinsPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespawnblacklist.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.deterrentlowphasedmg.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infestedgrassrarity.json");

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.conversionpathways.json");

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.blockbreakblacklist.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.foodstealblacklist.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.minferalisations.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermantp.json");

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adapteddespawnlock.json", () -> EarlyConfigReader.getInt("Adapted Despawn Penalty First Phase", SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadpenalty.json", () -> EarlyConfigReader.getInt("Biome Spreading Penalty First Phase", SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infsquidcap.json", () -> EarlyConfigReader.getInt("Water Parasite Mob Cap", SRPMixinsConfigHandler.waterparas.waterParasiteCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nexuscap.json", () -> EarlyConfigReader.getInt("Nexus Mob Cap", SRPMixinsConfigHandler.deterrents.nexusCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simbigspiderassimfix.json", () -> EarlyConfigReader.getInt("Sim Big Spider Min Assimilations", SRPMixinsConfigHandler.coth.assimBigSpiderMinAssimilations) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermancap.json", () -> EarlyConfigReader.getInt("End Simmermen Conversion Cap", SRPMixinsConfigHandler.simmermen.endSimmermenCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.spawninglight.json", () -> EarlyConfigReader.getInt("Min Blocklight Threshold", SRPMixinsConfigHandler.spawns.blockLightThresholdTwo) != 7);

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlast.json", () -> Loader.isModLoaded("overlast"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlast_customphases.json", () -> shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion.FULL));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlastlite_customphases.json", () -> shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion.LITE));
	}

	private boolean shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion versionCompare) {
		OverLastCompat.OverLastVersion version = OverLastCompat.getOverLastVersion();
		if(version != versionCompare) return false;

		boolean playerPhases = EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled);
		boolean chunkPhases = EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled);
		boolean overlastEnabled = EarlyConfigReader.getBoolean("Enable OverLast custom phases",SRPMixinsConfigHandler.modcompat.enableOverLastCustomPhases);

		return (playerPhases || chunkPhases) && overlastEnabled;
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