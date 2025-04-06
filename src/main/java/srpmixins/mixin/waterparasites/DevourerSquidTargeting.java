package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINearestAttackableTargetStatus;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*@Mixin(targets = "com.dhanantry.scapeandrunparasites.entity.ai.EntityAINearestAttackableTargetStatus$1")
public abstract class DevourerSquidTargeting<T extends EntityLivingBase> {
    @ModifyExpressionValue(
            method = "apply(Lnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(value = "INVOKE", target = "Lcom/google/common/base/Predicate;apply(Ljava/lang/Object;)Z"),
            remap = false
    )
    private boolean srpmixins_alsoAttackWaterMobs(boolean original, @Local(argsOnly = true) EntityLivingBase entity) {
        return original || entity instanceof EntityWaterMob;
    }
}*/ //This works but i actually want it to work differently

@Mixin(EntityLum.class)
public abstract class DevourerSquidTargeting extends EntityParasiteBase {
    public DevourerSquidTargeting(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "initEntityAI",
            at = @At("TAIL")
    )
    protected void applyBonuses(CallbackInfo ci){
        if (SRPConfig.mobattacking) {
            this.targetTasks.addTask(4, new EntityAINearestAttackableTargetStatus<>(
                    this,
                    EntityWaterMob.class,
                    0,
                    SRPConfig.primitiveWalls,
                    false,
                    entity -> !ParasiteEventEntity.checkEntity(entity, SRPConfig.mobattackingBlackList, SRPConfig.mobattackingBlackListWhite),
                    SRPConfig.primitiveSneakPen,
                    SRPConfig.primitiveInviPen
            ));
        }
    }
}
