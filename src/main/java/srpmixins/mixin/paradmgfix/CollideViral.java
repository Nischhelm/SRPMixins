package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.adapted.*;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityLodo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.*;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(value = {
        EntityBanoAdapted.class,
        EntityCanraAdapted.class,
        EntityNoglaAdapted.class,
        EntityRanracAdapted.class,
        EntityShycoAdapted.class,
        EntityBano.class,
        EntityCanra.class,
        EntityNogla.class,
        EntityRanrac.class,
        EntityShyco.class,
        EntityFlog.class,
        EntityLodo.class,
        EntityMudo.class,
        EntityNuuh.class
})
public abstract class CollideViral {
    @WrapWithCondition(
            method = "collideWithEntity",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPPotions;applyStackPotion(Lnet/minecraft/potion/Potion;Lnet/minecraft/entity/EntityLivingBase;II)V", remap = false)
    )
    private boolean srpmixins_applyViralLessOften(Potion effect, EntityLivingBase entity, int duration, int amp){
        return SRPMixinsConfigHandler.dmgfix.viralStackSpeedOnTouch == 0 || entity.ticksExisted % SRPMixinsConfigHandler.dmgfix.viralStackSpeedOnTouch == 0;
    }
}
