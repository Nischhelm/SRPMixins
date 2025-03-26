package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SRPEventHandlerBus.class)
public abstract class MobSpawningCOTH {
    @Inject(
            method = "setCOTH",
            at = @At("TAIL"),
            remap = false
    )
    private void srpmixins_useAllCOTHSpawnConfigs(EntityLivingBase target, byte evo, CallbackInfo ci, @Local Random rand){
        switch (evo) {
            case 9:
                if (rand.nextDouble() < SRPConfigSystems.mobSpawningCOTHChanceNine)
                    target.addPotionEffect(new PotionEffect(SRPPotions.COTH_E, 3600, 0, false, false));
                break;
            case 10:
                if (rand.nextDouble() < SRPConfigSystems.mobSpawningCOTHChanceTen)
                    target.addPotionEffect(new PotionEffect(SRPPotions.COTH_E, 3600, 0, false, false));
                break;
        }

    }
}
