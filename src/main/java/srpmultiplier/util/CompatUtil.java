package srpmultiplier.util;

import net.minecraftforge.fml.common.Loader;

public class CompatUtil {

    private static final String LYCANITESMOBS_MODID = "lycanitesmobs";
    private static Boolean isLycanitesMobsLoaded = null;

    public static boolean isLycanitesMobsLoaded() {
        if(isLycanitesMobsLoaded == null) isLycanitesMobsLoaded = Loader.isModLoaded(LYCANITESMOBS_MODID);
        return isLycanitesMobsLoaded;
    }
}
