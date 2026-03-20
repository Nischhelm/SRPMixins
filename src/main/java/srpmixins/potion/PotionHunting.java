package srpmixins.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import srpmixins.SRPMixins;

import javax.annotation.Nonnull;

public class PotionHunting extends Potion {
    public PotionHunting() {
        super(false, 0x000000);
        this.setRegistryName(SRPMixins.MODID, "hunting");
        this.setPotionName("effects." + SRPMixins.MODID + ".hunting");
    }

    @Override public boolean hasStatusIcon() { return false; }
    @Override public boolean shouldRenderHUD(@Nonnull PotionEffect effect) { return false; }
    @Override public boolean shouldRender(@Nonnull PotionEffect effect) { return false; }
    @Override public boolean shouldRenderInvText(@Nonnull PotionEffect effect) { return false; }
}
