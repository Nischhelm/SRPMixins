package srpmixins.compat;

import net.minecraftforge.fml.common.Loader;

public class CompatUtil {

    private static final String LYCANITESMOBS_MODID = "lycanitesmobs";
    private static Boolean isLycanitesMobsLoaded = null;
    private static final String BLOODMOON_MODID = "bloodmoon";
    private static Boolean isBloodMoonLoaded = null;
    private static final String COTESIA_MODID = "srpcotesia";
    private static Boolean isCotesiaLoaded = null;

    public static boolean isLycanitesMobsLoaded() {
        if(isLycanitesMobsLoaded == null) isLycanitesMobsLoaded = Loader.isModLoaded(LYCANITESMOBS_MODID);
        return isLycanitesMobsLoaded;
    }

    public static boolean isBloodMoonLoaded() {
        if(isBloodMoonLoaded == null) isBloodMoonLoaded = Loader.isModLoaded(BLOODMOON_MODID);
        return isBloodMoonLoaded;
    }

    public static boolean isCotesiaLoaded() {
        if(isCotesiaLoaded == null) isCotesiaLoaded = Loader.isModLoaded(COTESIA_MODID);
        return isCotesiaLoaded;
    }
}
