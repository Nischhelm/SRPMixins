package srpmixins.client;

import java.lang.reflect.Field;

/**
 * OptiFine's "Fog: Off" still runs setupFog normally and only disables GL fog later in RenderGlobal, for renderers whose fogStandard flag is set.
 */
final class OptiFineFog {
    private static final int FOG_OFF = 3;

    private final Field fogType;
    private final Field fogStandard;

    OptiFineFog(Class<?> gameSettingsType, Class<?> rendererType) {
        fogType = optionalField(gameSettingsType, "ofFogType");
        fogStandard = optionalField(rendererType, "fogStandard");
    }

    boolean disablesFog(Object gameSettings, Object renderer) {
        if (fogType == null || fogStandard == null) return false;
        try {
            return fogType.getInt(gameSettings) == FOG_OFF && fogStandard.getBoolean(renderer);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Cannot read OptiFine fog settings", exception);
        }
    }

    void keepFog(Object renderer) {
        if (fogStandard == null) return;
        try {
            fogStandard.setBoolean(renderer, false);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Cannot write OptiFine fog state", exception);
        }
    }

    private static Field optionalField(Class<?> type, String name) {
        try {
            return type.getField(name);
        } catch (NoSuchFieldException exception) {
            return null;
        }
    }
}
