package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityTerla;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileHomming;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(EntityProjectileHomming.class)
public abstract class OverlordHaunterRangedFix {
    @Shadow(remap = false) private EntityLivingBase owner;
    @Shadow(remap = false) private float damage;

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/Entity;F)V",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void fixRangedDmg(World worldIn, EntityLivingBase ownerIn, Entity targetIn, float damage, CallbackInfo ci){
        if (this.owner instanceof EntityTerla) this.damage = SRPMixinsConfigHandler.dmgfix.overlordProjectileDamage * SRPConfig.globalDamageMultiplier;
        if (this.owner instanceof EntityPheon) this.damage = SRPMixinsConfigHandler.dmgfix.haunterProjectileDamage * SRPConfig.globalDamageMultiplier;
        this.damage *= SRPMixinsConfigProvider.dimensionDmgMultipliers.getOrDefault(this.owner.world.provider.getDimension(),1F);
    }
}
