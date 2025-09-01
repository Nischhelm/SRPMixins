package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Fix idea by Monarch = Calamity Catastrophe dev
@Mixin(EntityPInfected.class)
public abstract class AssimTurnToFeralFix extends EntityParasiteBase {
    public AssimTurnToFeralFix(World worldIn) {
        super(worldIn);
    }

    @Unique private boolean srpmixins$hasSetMeltSearchAiTask = false;

    @ModifyConstant(
            method = "onLivingUpdate",
            constant = @Constant(doubleValue = 0.0F)
    )
    private double srpmixins_dontSetKillCountToZero(double constant){
        this.srpmixins$hasSetMeltSearchAiTask = true;
        return this.killcount;
    }

    @WrapWithCondition(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V")
    )
    private boolean srpmixins_dontSetKillCountToZero(EntityAITasks instance, int priority, EntityAIBase task){
        return !this.srpmixins$hasSetMeltSearchAiTask;
    }
}
