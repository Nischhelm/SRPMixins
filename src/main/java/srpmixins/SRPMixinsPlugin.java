package srpmixins;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmixins.handlers.SRPMixinsConfigHandler;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMixinsPlugin implements IFMLLoadingPlugin {

	public SRPMixinsPlugin() {
		MixinBootstrap.init();

		if(SRPMixinsConfigHandler.getBoolean("Use Player Phases"))
			FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.playerphases.json", true);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srparasites.json", true);
		if(SRPMixinsConfigHandler.getBoolean("Compat: Modpack has LostCities mod")) {
			FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.lostcities.json", true);
			if (SRPMixinsConfigHandler.getBoolean("Compat: Modpack has Bloodmoon mod"))
				FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.bloodmoon.json", true);
		}

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