package srpmixins.compat.overlast;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import srpmixins.SRPMixinsPlugin;
import srpmixins.compat.CompatUtil;
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

        boolean customPhases = SRPMixinsPlugin.areCustomPhasesEnabled();
        boolean overlastEnabled = EarlyConfigReader.getBoolean("Enable OverLast custom phases",SRPMixinsConfigHandler.modcompat.enableOverLastCustomPhases);

        return customPhases && overlastEnabled;
    }
}
