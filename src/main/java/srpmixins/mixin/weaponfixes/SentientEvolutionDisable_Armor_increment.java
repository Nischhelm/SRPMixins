package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPEventHandlerBus.class)
public abstract class SentientEvolutionDisable_Armor_increment {

    @WrapWithCondition(
            method = "entityHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setInteger(Ljava/lang/String;I)V")
    )
    private boolean srpmixins_disableIncrementSRPKills(NBTTagCompound instance, String key, int value){
        return !key.equals("srphits");
    }
}