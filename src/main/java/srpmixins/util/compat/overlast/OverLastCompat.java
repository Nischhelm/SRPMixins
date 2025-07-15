package srpmixins.util.compat.overlast;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

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
}
