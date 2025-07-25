package srpmixins.mixin.modcompat.srpdeepseadanger.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import energon.srpdeepseadanger.inject.EntityInject;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(EntityInject.class)
public abstract class EntityInjectMixin {
    @Redirect(
            method = "playerFishing",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_useCustomPhases(World world, @Local(argsOnly = true) ItemFishedEvent event){
        return SRPSaveDataInterface.get(world, event.getEntityPlayer(), null);
    }
}
