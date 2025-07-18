package srpmixins.mixin.modcompat.overlast.hudrender;

import com.overlast.gui.StatBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import srpmixins.util.compat.overlast.IStatBar;

@Mixin(StatBar.class)
public abstract class StatBarMixin implements IStatBar {
    @Shadow(remap = false) private int defaultBarWidth;
    @Shadow(remap = false) private float maxValue;
    @Shadow(remap = false) private float value;
    @Shadow(remap = false) private StatBar.StatType type;

    /**
     * @author Nischhelm
     * @reason stupid
     */
    @Overwrite(remap = false)
    public String getTextToDisplay() {
        int points = (int) this.value;
        String text;
        if (this.type == StatBar.StatType.EVOLUTION) {
            if (points >= 10000000) text = points/1000000 + "m";
            else if (points >= 10000) text = points/1000 + "k";
            else text = ""+points;
            return text + "P";
        } else {
            float roundedValue = Math.round(this.value * 10) / 10F;
            return Float.toString(roundedValue);
        }
    }

    @Unique private float srpmixins$minValue = 0;

    @Override
    public void srpmixins$setMinValue(float minValue) {
        this.srpmixins$minValue = minValue;
    }

    /**
     * @author Nischhelm
     * @reason stupid
     */
    @Overwrite(remap = false)
    public int getMovingWidth() {
        return (int)(this.defaultBarWidth * (this.value - this.srpmixins$minValue) / (this.maxValue - this.srpmixins$minValue));
    }
}
