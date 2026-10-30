package srpmixins.client;

import net.minecraft.block.state.IBlockState;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class SRPFog {
    public static boolean terrainFogEnabled() {
        return Settings.options.enabled(Minecraft.getMinecraft().gameSettings);
    }

    private static final class Settings {
        private static final FogSettings options = new FogSettings(
                Minecraft.getMinecraft().gameSettings.getClass(), FogSettings.celeritasQuality());
    }

    public static boolean canApplyFog(Entity entity, IBlockState state) {
        return !state.getMaterial().isLiquid()
                && !(entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS));
    }

    public static void applyAmbientFog(float density) {
        Ambient.apply(density);
    }

    private static final class Ambient {
        private static final Object state = state();
        private static final Field mode = ReflectionHelper.findField(state.getClass(), "mode", "field_179047_b");
        private static final Field start = ReflectionHelper.findField(state.getClass(), "start", "field_179045_d");
        private static final Field end = ReflectionHelper.findField(state.getClass(), "end", "field_179046_e");
        private static final FogRange range = new FogRange();

        private static Object state() {
            try {
                return ReflectionHelper.findField(GlStateManager.class, "fogState", "field_179155_g").get(null);
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Cannot access ambient fog state", exception);
            }
        }

        private static void apply(float density) {
            try {
                if (mode.getInt(state) != GL11.GL_LINEAR) return;
                float ambientStart = start.getFloat(state);
                float ambientEnd = end.getFloat(state);
                if (!Float.isFinite(ambientStart) || !Float.isFinite(ambientEnd) || ambientEnd <= 0.0F || ambientStart >= ambientEnd) return;
                range.compose(ambientStart, ambientEnd, density, SRPConfigWorld.biomeFogDensity);
                GlStateManager.setFogStart(range.getStart());
                GlStateManager.setFogEnd(range.getEnd());
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Cannot read ambient fog range", exception);
            }
        }
    }

    /**
     * Client-side holder of the current SRP fog density (smoothed via {@link Transition}), reset per server connection instead of per world load.
     */
    public static final class State {
        private static WeakReference<Object> connection = new WeakReference<>(null);
        private static Transition transition = new Transition();

        private State() {
        }

        private static void checkConnection() {
            Object current = Minecraft.getMinecraft().getConnection();
            if (current == null || connection.get() != current) {
                connection = new WeakReference<>(current);
                transition = new Transition();
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
    }

    /**
     * Math only: eases the displayed density towards the last density received from the server (exponential, ~0.15s time constant),
     * and converts density into a fog color blend weight.
     */
    public static final class Transition {
        private static final double SMOOTHING_TIME_SECONDS = 0.15;
        private static final float ZERO_DENSITY_THRESHOLD = 1.0e-7F;
        private static final float FULL_COLOR_DENSITY_FRACTION = 0.2F;

        private float value;
        private float target;
        private long sampledAt;
        private boolean initialized;

        public void receive(float density, long now) {
            sample(now);
            target = Float.isFinite(density) ? Math.max(0.0F, density) : 0.0F;
        }

        public float sample(long now) {
            if (initialized) {
                double elapsed = Math.max(0.0, (now - sampledAt) * 1.0e-9);
                value += (target - value) * (float) -Math.expm1(-elapsed / SMOOTHING_TIME_SECONDS);
                if (target == 0.0F && value < ZERO_DENSITY_THRESHOLD) value = 0.0F;
            }
            sampledAt = now;
            initialized = true;
            return value;
        }

        public static float colorWeight(float density, float maximum) {
            if (!Float.isFinite(maximum) || maximum <= 0.0F) return 0.0F;
            float weight = Math.max(0.0F, Math.min(1.0F, density / maximum / FULL_COLOR_DENSITY_FRACTION));
            return weight * weight * (3.0F - 2.0F * weight);
        }
    }
}
