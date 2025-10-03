package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIAvoidEntityStatus;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.google.common.base.Predicate;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityMudo.class)
public abstract class RupterAvoidsPlayers extends Entity {
    public RupterAvoidsPlayers(World worldIn) {
        super(worldIn);
    }

    @WrapOperation(
            method = "applyBonuses",
            at = @At(value = "NEW", target = "(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;Ljava/lang/Class;Lcom/google/common/base/Predicate;FD)Lcom/dhanantry/scapeandrunparasites/entity/ai/EntityAIAvoidEntityStatus;"),
            remap = false
    )
    private EntityAIAvoidEntityStatus<EntityLivingBase> srpmixins_rupterAvoidsPlayers(EntityParasiteBase entityIn, Class<? extends Entity> classToAvoidIn, Predicate<? super Entity> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, Operation<EntityAIAvoidEntityStatus<EntityLivingBase>> original){
        return new EntityAIAvoidEntityStatus<>(
                entityIn,
                EntityLivingBase.class,
                entity -> !(entity instanceof EntityParasiteBase) &&
                        !(entity instanceof EntityAnimal) &&
                        !(entity instanceof EntityVillager) &&
                        !(entity instanceof EntityWaterMob) &&
                        !(entity instanceof EntityAmbientCreature) &&
                        !(entity instanceof EntityArmorStand),
                avoidDistanceIn, farSpeedIn);
    }
}
