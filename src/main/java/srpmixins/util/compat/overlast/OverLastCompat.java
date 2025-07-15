package srpmixins.util.compat.overlast;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

public class OverLastCompat {
    public enum OverLastVersion {
        FULL,
        LITE,
        NONE
    }

    public static OverLastVersion getOverLastVersion(){
        ModContainer overlastMod = Loader.instance().getIndexedModList().get("overlast");
        if(overlastMod == null) return OverLastVersion.NONE;

        if(overlastMod.getVersion().matches("^0\\.0\\.\\d+$")) //lite: 0.0.x
            return OverLastVersion.LITE;
        else
            return OverLastVersion.FULL;
    }

    public static boolean shouldEnqueueOverLastMixins(OverLastVersion versionCompare) {
        OverLastVersion version = getOverLastVersion();
        if(version != versionCompare) return false;

        boolean playerPhases = EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled);
        boolean chunkPhases = EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled);
        boolean overlastEnabled = EarlyConfigReader.getBoolean("Enable OverLast custom phases",SRPMixinsConfigHandler.modcompat.enableOverLastCustomPhases);

        return (playerPhases || chunkPhases) && overlastEnabled;
    }
}
