package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.event.WorldMobCapEvent;
import srpmixins.util.ParasiteCreatureType;

import java.util.Collections;

@Mixin(SRPCommandEvolution.class)
public abstract class SRPCommandEvolutionMixin {

    @WrapOperation(
            method = "execute",
            at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;append(I)Ljava/lang/StringBuilder;", ordinal = 5)
    )
    private StringBuilder srpmixins_modifyMobCap(StringBuilder instance, int mobCap, Operation<StringBuilder> original, @Local(name = "count") int count, @Local(argsOnly = true) ICommandSender sender){
        WorldMobCapEvent event = new WorldMobCapEvent(sender.getEntityWorld(), ParasiteCreatureType.PARASITE, Collections.emptySet(), count, 0, mobCap);
        MinecraftForge.EVENT_BUS.post(event);
        return original.call(instance, event.getMobCap());
    }
}