package srpmixins.compat;

import net.minecraftforge.fml.common.Loader;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

public class CompatUtil {

    public static final LoadedContainer lycanitesmobs = new LoadedContainer("lycanitesmobs");
    public static final LoadedContainer bloodMoon = new LoadedContainer("bloodmoon");
    public static final LoadedContainer cotesia = new LoadedContainer("srpcotesia");
    public static final LoadedContainer srpextra = new LoadedContainer("srpextra");

    public static class LoadedContainer{
        private Boolean isLoaded = null;
        private final String key;
        private LoadedContainer(String key){
            this.key = key;
        }
        public boolean isLoaded(){
            if(this.isLoaded == null) isLoaded = Loader.isModLoaded(key);
            return isLoaded;
        }
    }

    public static boolean areCustomPhasesEnabled() {
        boolean playerPhases = EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled);
        boolean chunkPhases = EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled);
        return (playerPhases || chunkPhases);
    }
}
