package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.network.SRPPacketFog;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(SRPEventHandlerBus.class)
public abstract class BiomeFogFix {
    @Shadow(remap = false) public static float fog;
    @Unique private static final Map<UUID, Float> srpmixins$playerFogDensities = new HashMap<>();

    @WrapWithCondition(
            method = "playerTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/network/simpleimpl/SimpleNetworkWrapper;sendTo(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;Lnet/minecraft/entity/player/EntityPlayerMP;)V"),
            remap = false
    )
    private boolean srpmixins_dontSendOriginalPacket(SimpleNetworkWrapper instance, IMessage message, EntityPlayerMP player){
        return !(message instanceof SRPPacketFog);
    }

    @ModifyExpressionValue(
            method = "playerTick",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventWorld;canBiomeStillExist(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)I"),
            remap = false
    )
    private int srpmixins_handleBiomeFogPerPlayer(int original, @Local boolean isParaBiome, @Local EntityPlayer player){
        UUID playerUUID = player.getUniqueID();
        float playerFog = srpmixins$playerFogDensities.getOrDefault(playerUUID, 0.0F);
        if (original < 1 && !isParaBiome) {
            //Reduce fog down to 0
            if (playerFog > 0.0F) {
                playerFog = Math.max(playerFog - SRPConfigWorld.biomeFogDensity / 75, 0.0F);
                SRPMain.network.sendTo(new SRPPacketFog(playerFog), (EntityPlayerMP)player);
                srpmixins$playerFogDensities.put(playerUUID, playerFog);
            }
        } else if (playerFog < SRPConfigWorld.biomeFogDensity) {
            //increase fog up to biomeFogDensity
            playerFog = Math.min(playerFog + SRPConfigWorld.biomeFogDensity / 128, SRPConfigWorld.biomeFogDensity);
            SRPMain.network.sendTo(new SRPPacketFog(playerFog), (EntityPlayerMP) player);
            srpmixins$playerFogDensities.put(playerUUID, playerFog);
        }

        return original;
    }

    @WrapOperation(
            method = "playerTick",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"),
            remap = false
    )
    private float srpmixins_dontChangeFog_max(float a, float b, Operation<Float> original){
        //This is just for single player if the config values are changed, doesn't matter for multi
        return fog;
    }

    @WrapOperation(
            method = "playerTick",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"),
            remap = false
    )
    private float srpmixins_dontChangeFog_min(float a, float b, Operation<Float> original){
        //This is just for single player if the config values are changed, doesn't matter for multi
        return fog;
    }
}
