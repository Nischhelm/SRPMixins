package srpmixins.mixin.morescents;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.providers.MoreScentsConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin_DeathValue {
    @ModifyReturnValue(method = "getCCDeathValue", at = @At("RETURN"), remap = false)
    private int srpmixins_modifyCCDeathValue(int original) {
        int paraId = ((EntityParasiteBase)(Object)this).getParasiteIDRegister();

        String paraName = SRPMobConfigProvider.paraIdToMobName.getOrDefault(paraId,"");
        if(!paraName.contains(":")) paraName = "srparasites:"+paraName;

        if(MoreScentsConfigProvider.deathValues.containsKey(paraName))
            return MoreScentsConfigProvider.deathValues.get(paraName);

        String paraGroup = SRPMobConfigProvider.getParaGroup(paraId);
        if(!paraGroup.isEmpty() && MoreScentsConfigProvider.deathValues.containsKey(paraGroup))
            return MoreScentsConfigProvider.deathValues.get(paraGroup);

        return original;
    }
}
