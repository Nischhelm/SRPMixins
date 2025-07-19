package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigProvider;

import java.util.Collections;
import java.util.Set;

@Mixin(EntityParasiteBase.class)
public abstract class BlockBreakBlacklist {
    @WrapOperation(
            method = "skillBreakBlocks",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;blockException(Ljava/lang/String;)Z"),
            remap = false
    )
    private boolean srpmixins_blacklistBlockByParasite(EntityParasiteBase parasite, String blockId, Operation<Boolean> original) {
        int paraId = parasite.getParasiteIDRegister();
        Set<Integer> listedParaIds = SRPMixinsConfigProvider.blockBreakBlacklist.getOrDefault(blockId, Collections.emptySet());
        //fail test if block is blacklisted for this parasite
        if(listedParaIds.contains(paraId)) return true;
        return original.call(parasite, blockId);
    }
}
