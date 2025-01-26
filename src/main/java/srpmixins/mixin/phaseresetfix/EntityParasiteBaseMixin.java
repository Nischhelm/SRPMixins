package srpmixins.mixin.phaseresetfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin extends EntityLiving {

    public EntityParasiteBaseMixin(World worldIn) {
        super(worldIn);
    }

    @Unique private static IEntityLivingData savedData;

    @Redirect(
            method = "func_180482_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/EntityMob;func_180482_a(Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/IEntityLivingData;)Lnet/minecraft/entity/IEntityLivingData;"),
            remap = false
    )
    IEntityLivingData saveReturnValue(EntityMob instance, DifficultyInstance difficulty, IEntityLivingData livingdata){
        IEntityLivingData floo = super.onInitialSpawn(difficulty, livingdata);
        if(this.world.isRemote)
            savedData = floo;
        return floo;
    }


    @Inject(
            method = "func_180482_a",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false,
            cancellable = true)
    void skipIfClient(DifficultyInstance difficulty, IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> cir){
        if(this.world.isRemote)
            cir.setReturnValue(savedData);
    }
}