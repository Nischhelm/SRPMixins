package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.util.IParasite;
import srpmixins.util.ParasiteCreatureType;

@Mixin(EntityParasiteBase.class)
public abstract class CountParasiteBaseMobs extends EntityLiving implements IParasite {
    //makes all paras IParasite and fix the isCreatureType override

    public CountParasiteBaseMobs(World worldIn) {
        super(worldIn);
    }

    @ModifyVariable(
            method = "isCreatureType",
            at = @At(value = "LOAD"),
            name = "forSpawnCount",
            remap = false
    )
    private boolean srpmixins_countForParaMobCap(boolean original, @Local(argsOnly = true) EnumCreatureType type){
        return original && type != ParasiteCreatureType.PARASITE;
    }
}
