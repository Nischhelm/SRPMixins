package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPEffectBase.class)
public abstract class SRPEffectBaseMixin {
    @Redirect(
            method="effectCOTH",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData srpmixins_getPlayerData(World world, @Local(argsOnly = true) EntityLivingBase entity){
        return SRPSaveDataInterface.get(world,null,entity.getPosition());
    }

    @Redirect(
            method="effectPrey",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData srpmixins_getPlayerData2(World world, @Local(argsOnly = true) EntityLivingBase entity){
        return SRPSaveDataInterface.get(world,null,entity.getPosition());
    }

}