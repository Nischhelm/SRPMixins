package srpmixins.mixin.oe_tmp;

import com.sirsquidly.oe.entity.item.EntityTrident;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(EntityTrident.class)
public abstract class EntityTridentMixin {
    @Shadow(remap = false) public Entity trueOwner;

    @Inject(
            method = "writeEntityToNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getUniqueID()Ljava/util/UUID;"),
            cancellable = true
    )
    private void srpmixins_dontWriteNullStrings(NBTTagCompound compound, CallbackInfo ci){
        if(this.trueOwner == null || !(this.trueOwner instanceof EntityPlayer)) ci.cancel();
    }
}
