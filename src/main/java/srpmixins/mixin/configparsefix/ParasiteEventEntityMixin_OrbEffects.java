package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityWymo;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPConfigProvider;
import srpmixins.util.ParaOrbEffect;

import java.util.List;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin_OrbEffects {
    @Inject(
            method = "orbApplyEffects",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void orbApplyEffects(EntityLivingBase target, EntityParasiteBase orbCreator, String[] unused, int mobCount, CallbackInfo ci) {
        int paraId = orbCreator.getParasiteIDRegister();
        if (paraId == 37 && orbCreator instanceof EntityWymo)
            paraId = SRPConfigProvider.WYMO_FAKEID; //WYMO fix with fake id

        List<ParaOrbEffect> orbEffects = SRPConfigProvider.orbEffects.get(paraId);
        if (orbEffects == null) return; //other mobs not from base SRP -> fallback to default less performant behavior

        boolean targetIsPara = target instanceof EntityParasiteBase;

        for (ParaOrbEffect orbEffect : orbEffects) {
            int duration = orbEffect.effect.getDuration();
            int amp = orbEffect.effect.getAmplifier();
            Potion potion = orbEffect.effect.getPotion();

            if (orbEffect.mobDivisorAmplifier != 0) amp += mobCount / orbEffect.mobDivisorAmplifier;
            if (orbEffect.mobDivisorDuration != 0) duration += mobCount / orbEffect.mobDivisorDuration * 20;

            switch (orbEffect.applicationMode) {
                case ParaOrbEffect.SELF:
                    orbCreator.addPotionEffect(new PotionEffect(potion, duration, amp, false, false));
                    break;
                case ParaOrbEffect.PARAS:
                    if (targetIsPara) SRPPotions.applyStackPotion(potion, target, duration, amp);
                    break;
                case ParaOrbEffect.OTHER:
                default:
                    if (!targetIsPara) SRPPotions.applyStackPotion(potion, target, duration, amp);
                    break;
            }
        }

        ci.cancel();
    }
}
