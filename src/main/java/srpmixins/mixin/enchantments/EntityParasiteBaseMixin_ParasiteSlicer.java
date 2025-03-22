package srpmixins.mixin.enchantments;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import srpmixins.util.ParasiteCreatureAttribute;

import javax.annotation.Nonnull;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin_ParasiteSlicer extends EntityLivingBase {
    public EntityParasiteBaseMixin_ParasiteSlicer(World worldIn) {
        super(worldIn);
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return ParasiteCreatureAttribute.PARASITE;
    }
}