package srpmixins.client;

import java.lang.reflect.Field;

final class FogSettings {
    private final Field optifineFogType;
    private final Object celeritasQuality;
    private final Field celeritasTerrainFog;

    FogSettings(Class<?> gameSettingsType, Object celeritasQuality) {
        optifineFogType = optionalField(gameSettingsType, "ofFogType");
        this.celeritasQuality = celeritasQuality;
        celeritasTerrainFog = celeritasQuality == null ? null : optionalField(celeritasQuality.getClass(), "terrainFog");
    }

    boolean enabled(Object gameSettings) {
        try {
            return (optifineFogType == null || optifineFogType.getInt(gameSettings) != 3)
                    && (celeritasTerrainFog == null || celeritasTerrainFog.getBoolean(celeritasQuality));
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Cannot read terrain fog settings", exception);
        }
    }

    static Object celeritasQuality() {
        try {
            Class<?> renderer = Class.forName("org.taumc.celeritas.CeleritasVintage");
            Object options = renderer.getMethod("options").invoke(null);
            return options.getClass().getField("quality").get(options);
        } catch (ClassNotFoundException exception) {
            return null;
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Cannot read Celeritas terrain fog settings", exception);
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
