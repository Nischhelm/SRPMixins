package srpmixins.mixin.morescents;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityRof;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.providers.MoreScentsConfigProvider;

@Mixin(EntityRof.class)
public abstract class EntityRofMixin_MoreScents extends EntityLivingBase {
    public EntityRofMixin_MoreScents(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) public byte dangerArea;

    @ModifyReturnValue(method = "getMob", at = @At(value = "RETURN"), remap = false)
    private String srpmixins_modifyScentSpawnListsViaMoreScents(String original){
        return MoreScentsConfigProvider.getRandomWaveSpawnListEntry(this.getRNG(), this.dangerArea);
    }
}
