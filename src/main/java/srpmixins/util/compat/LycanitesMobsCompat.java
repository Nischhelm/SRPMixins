package srpmixins.util.compat;

import com.lycanitesmobs.core.spawner.SpawnerManager;

public class LycanitesMobsCompat {
    public static void reloadLycaniteSpawnerManager(){
        SpawnerManager.getInstance().reload();
    }
}
