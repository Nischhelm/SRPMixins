package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPFeral;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(ParasiteEventEntity.class)
public abstract class MinFeralisations_AddCount {
    @Inject(
            method = "convertEntityFeral",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setDead()V")
    )
    private static void srpmixins_minFeralisations(EntityLivingBase entityin, NBTTagCompound tags, boolean ignoreKey, String[] list, CallbackInfoReturnable<Boolean> cir, @Local EntityPFeral feral, @Local World world){
        if(SRPMixinsConfigProvider.minFeralisations.containsKey(feral.getParasiteIDRegister()))
            SRPSaveData.get(world).addNumberIDDataSpawn(feral.getParasiteIDRegister());

    }
}
