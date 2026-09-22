package srpmixins.client.fog;

import net.minecraft.client.Minecraft;

import java.lang.ref.WeakReference;

public final class SrpFogState {
    private static WeakReference<Object> connection = new WeakReference<>(null);
    private static SrpFogTransition transition = new SrpFogTransition();
    private static double pendingFogFrame = Double.NaN;

    private SrpFogState() {
    }

    private static void checkConnection() {
        Object current = Minecraft.getMinecraft().getConnection();
        if (current == null || connection.get() != current) {
            connection = new WeakReference<>(current);
            transition = new SrpFogTransition();
        }
    }

    public static void receive(float density) {
        checkConnection();
        transition.receive(density, System.nanoTime());
    }

    public static float density() {
        checkConnection();
        return transition.sample(System.nanoTime());
    }

    public static void prepareFogFrame(double frame) {
        pendingFogFrame = frame;
    }

    public static void clearFogFrame() {
        pendingFogFrame = Double.NaN;
    }

    public static boolean consumeFogFrame(double frame) {
        boolean pending = pendingFogFrame == frame;
        pendingFogFrame = Double.NaN;
        return pending;
    }
}
