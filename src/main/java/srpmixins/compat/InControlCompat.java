package srpmixins.compat;

import mcjty.tools.typed.Key;
import mcjty.tools.typed.Type;

public class InControlCompat {
    public static final Key<Integer> MINEVOPHASE = Key.create(Type.INTEGER, "srp_minphase");
    public static final Key<Integer> MAXEVOPHASE = Key.create(Type.INTEGER, "srp_maxphase");
    public static final Key<Integer> MINNODES = Key.create(Type.INTEGER, "srp_minnodes");
    public static final Key<Integer> MAXNODES = Key.create(Type.INTEGER, "srp_maxnodes");
    public static final Key<Integer> MINCOLOS = Key.create(Type.INTEGER, "srp_mincolonies");
    public static final Key<Integer> MAXCOLOS = Key.create(Type.INTEGER, "srp_maxcolonies");
}
