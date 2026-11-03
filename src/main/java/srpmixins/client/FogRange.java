package srpmixins.client;

public final class FogRange {
    private static final double OPAQUE_OPTICAL_DEPTH = -Math.log(0.01);

    private float start;
    private float end;

    public void compose(float ambientStart, float ambientEnd, float density, float maximum) {
        if (maximum <= 0.0F) {
            start = ambientStart;
            end = ambientEnd;
            return;
        }
        double weight = Math.max(0.0, Math.min(1.0, density / (double) maximum));
        double scale = 1.0 + weight * Math.max(0.0, ambientEnd * (double) maximum / OPAQUE_OPTICAL_DEPTH - 1.0);
        start = Math.min(ambientStart, (float) (ambientStart * (1.0 - weight) / scale));
        end = (float) (ambientEnd / scale);
    }

    public float getStart() {
        return start;
    }

    public float getEnd() {
        return end;
    }
}
