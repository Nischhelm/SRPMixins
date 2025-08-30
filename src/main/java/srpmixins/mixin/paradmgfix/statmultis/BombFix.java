package srpmixins.mixin.paradmgfix.statmultis;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHost;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHostII;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOmboo;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityJinjo;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityBomb;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.config.providers.DimensionMultiConfigProvider;

import javax.annotation.Nullable;

@Mixin(EntityBomb.class)
public abstract class BombFix {
    @Shadow(remap = false) @Nullable private EntityParasiteBase tntPlacedBy;

    @ModifyVariable(
            method = "setDamage",
            at = @At(value = "HEAD"),
            remap = false,
            argsOnly = true
    )
    private float srpmixins_fixProjDmg(float in){
        if(tntPlacedBy == null) return in;
        if(tntPlacedBy instanceof EntityJinjo) return in; //Heavy Bomber already does it correctly

        //These fuckers use their own bomb dmg values
        if(tntPlacedBy instanceof EntityOmboo || tntPlacedBy instanceof EntityHost || tntPlacedBy instanceof EntityHostII) {
            IAttributeInstance atkAttr = tntPlacedBy.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
            double baseValue = atkAttr.getBaseValue();
            if (baseValue != 0) {
                atkAttr.setBaseValue(in);
                float returnValue = (float) atkAttr.getAttributeValue();
                atkAttr.setBaseValue(baseValue);
                return returnValue;
            } else {
                //ignore stat rules
                int dimension = tntPlacedBy.dimension;
                return in * DimensionMultiConfigProvider.getDmgMap().getOrDefault(dimension, 1F);
            }
        } else
            //Will auto use stat rules and dimension multis for all nexus paras and for prim vermin (iki)
            return (float) tntPlacedBy.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
    }
}
