package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPSaveData.class)
public abstract class DefaultCanLose {
    @Unique private static boolean srpmixins$readingOldData = false;

    @WrapOperation(
            method = "createData",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setLoss(ZI)V"),
            remap = false
    )
    private static void srpmixins_phaseMinusTwoCantLose(SRPSaveData instance, boolean b, int in, Operation<Void> original){
        original.call(instance, false, in);
    }

    //------------- READ + WRITE new value

    @WrapOperation(
            method = "readFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;getTagList(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;")
    )
    private NBTTagList srpmixins_readNewValues(NBTTagCompound instance, String key, int type, Operation<NBTTagList> original){
        if(key.equals("srpevolutionloss")) {
            if(!instance.hasKey("srpevolutionloss"))
                return original.call(instance, "srpevolutionlose", type);

            srpmixins$readingOldData = true;
        }
        return original.call(instance, key, type);
    }

    @WrapOperation(
            method = "readFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;getBoolean(Ljava/lang/String;)Z")
    )
    private boolean srpmixins_invertOldReadValues(NBTTagCompound instance, String key, Operation<Boolean> original){
        if(srpmixins$readingOldData) return !original.call(instance, key);
        return original.call(instance, key);
    }

    @Inject(
            method = "readFromNBT",
            at = @At("RETURN")
    )
    private void srpmixins_resetValue(NBTTagCompound compound, CallbackInfo ci){
        srpmixins$readingOldData = false;
    }

    @WrapOperation(
            method = "writeToNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setTag(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V")
    )
    private void srpmixins_writeNewValues(NBTTagCompound instance, String key, NBTBase value, Operation<Void> original){
        if(key.equals("srpevolutionloss")) original.call(instance, "srpevolutionlose", value);
        else original.call(instance, key, value);
    }
}
