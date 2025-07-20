package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.ArrayList;

@Mixin(SRPSaveData.class)
public abstract class FixParaUnlocking extends WorldSavedData {
    public FixParaUnlocking(String name) {
        super(name);
    }

    @Shadow(remap = false) private ArrayList<Integer> lockedParasites;
    @Shadow(remap = false) private static SRPSaveData instance;

    @Inject(
            method = "resetLock",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void srpmixins_useCachedUnlockList_onReset(CallbackInfo ci){
        this.lockedParasites.clear();
        this.lockedParasites.addAll(SRPConfigProvider.lockedParasites);
        this.markDirty();
        ci.cancel();
    }

    @WrapOperation(
            method = "createData",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;evolutionParasiteLock:[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontUseOldList_onCreate(Operation<String[]> original){
        return new String[]{};
    }

    @Inject(
            method = "createData",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;evolutionDimStart:[Ljava/lang/String;", ordinal = 0),
            remap = false
    )
    private static void srpmixins_useCachedUnlockList_onCreate(World world, MapStorage storage, CallbackInfoReturnable<SRPSaveData> cir){
        instance.getLockedList().addAll(SRPConfigProvider.lockedParasites);
    }

    @WrapOperation(
            method = "checkForUnlock",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int srpmixins_useCachedUnlockList_onCheckForUnlock(String s, Operation<Integer> original){
        if(SRPMobConfigProvider.mobNameToParaIdMap.containsKey(s))
            return SRPMobConfigProvider.mobNameToParaIdMap.get(s);
        return original.call(s);
    }

    @ModifyExpressionValue(
            method = "checkForUnlock",
            at = @At(value = "INVOKE", target = "Ljava/lang/Byte;parseByte(Ljava/lang/String;)B"),
            remap = false
    )
    private byte srpmixins_alsoAllowHigherPhases(byte original, @Local(argsOnly = true) byte phase){
        //SRP only checks whether currphase == lockphase, we want currphase >= lockphase
        if(phase > original) return phase;
        return original;
    }
}
