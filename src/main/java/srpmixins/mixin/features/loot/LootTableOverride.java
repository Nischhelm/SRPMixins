package srpmixins.mixin.features.loot;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import srpmixins.config.providers.LootPoolProvider;

@Mixin(EntityParasiteBase.class)
public abstract class LootTableOverride extends EntityLiving {
    public LootTableOverride(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) public abstract int getParasiteIDRegister();

    @Override
    public ResourceLocation getLootTable(){
        return LootPoolProvider.lootTableLocations.get(this.getParasiteIDRegister());
    }
}
