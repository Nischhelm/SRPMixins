package srpmixins.compat;

import lumien.bloodmoon.server.BloodmoonHandler;

public class BloodMoonCompat {
    public static boolean isBloodMoonActive(){
        return BloodmoonHandler.INSTANCE.isBloodmoonActive();
    }
}
