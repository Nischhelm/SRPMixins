package srpmixins.util.configparse;

import net.minecraft.potion.PotionEffect;

public class ParaOrbEffect {
    public static final int OTHER = 0;
    public static final int SELF = 1;
    public static final int PARAS = 2;

    public int applicationMode;
    public PotionEffect effect;
    public int mobDivisorAmplifier;
    public int mobDivisorDuration;

    public ParaOrbEffect(int applicationMode, PotionEffect effect, int mobDivisorAmplifier, int mobDivisorDuration){
        this.applicationMode = applicationMode;
        this.effect = effect;
        this.mobDivisorAmplifier = mobDivisorAmplifier;
        this.mobDivisorDuration = mobDivisorDuration;
    }
}
