package srpmultiplier;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.CompatUtil;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMultiplierPlugin implements IFMLLoadingPlugin {

	public SRPMultiplierPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmultiplier.playerphases.json", true);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmultiplier.srparasites.json", true);
		if(SRPMultiplierConfigHandler.getBoolean("Compat: Modpack has LostCities mod")) {
			FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmultiplier.lostcities.json", true);
			if (SRPMultiplierConfigHandler.getBoolean("Compat: Modpack has Bloodmoon mod"))
				FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmultiplier.bloodmoon.json", true);
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