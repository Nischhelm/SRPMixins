package srpmixins;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMixinsPlugin implements IFMLLoadingPlugin {

	public SRPMixinsPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.registerAnnotatedMixinConfig(SRPMixinsConfigHandler.class, null);

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