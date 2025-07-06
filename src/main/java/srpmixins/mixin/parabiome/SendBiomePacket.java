package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasite;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.network.SRPMixinsPacketBiomeChange;

@Mixin(ParasiteEventWorld.class)
public abstract class SendBiomePacket {
    @Unique private static SRPMixinsPacketBiomeChange srpmixins$biomePacket = null;
    @Unique private static Biome srpmixins$biome = null;

    @ModifyExpressionValue(
            method = "SpreadBiome",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;")
    )
    private static Biome srpmixins_saveBiome(Biome original){
        srpmixins$biome = original;
        return original;
    }

    @WrapOperation(
            method = "SpreadBiome",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;checkName(Ljava/lang/String;[Ljava/lang/String;Z)Z"),
            remap = false
    )
    private static boolean srpmixins_dontAddIfAlreadyParaBiome(String biomeName, String[] biomeBlacklist, boolean isWhitelist, Operation<Boolean> original){
        if(srpmixins$biome instanceof BiomeParasite)
            return true; //fail check, don't add block if its already para biome
        return original.call(biomeName, biomeBlacklist, isWhitelist);
    }

    @WrapWithCondition(
            method = "SpreadBiome",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/network/simpleimpl/SimpleNetworkWrapper;sendToDimension(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;I)V"),
            remap = false
    )
    private static boolean srpmixins_addBlockPosToPacket(SimpleNetworkWrapper instance, IMessage message, int dimensionId, @Local(name = "convert") BlockPos convertedPos){
        if(srpmixins$biomePacket == null)
            srpmixins$biomePacket = new SRPMixinsPacketBiomeChange(true);

        srpmixins$biomePacket.addBlockPos(convertedPos);
        return false;
    }

    @Inject(
            method = "SpreadBiome",
            at = @At(value = "TAIL"),
            remap = false
    )
    private static void srpmixins_sendBiomePacket(World worldIn, BlockPos pos, int age, CallbackInfo ci){
        if(srpmixins$biomePacket != null) {
            SRPMain.network.sendToDimension(srpmixins$biomePacket, worldIn.provider.getDimension());
            srpmixins$biomePacket = null;
        }
    }

    @WrapWithCondition(
            method = "killBiome",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/network/simpleimpl/SimpleNetworkWrapper;sendToDimension(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;I)V"),
            remap = false
    )
    private static boolean srpmixins_addBlockPosToPacket_kill(SimpleNetworkWrapper instance, IMessage message, int dimensionId, @Local(name = "convert") BlockPos convertedPos){
        if(srpmixins$biomePacket == null)
            srpmixins$biomePacket = new SRPMixinsPacketBiomeChange(false);

        srpmixins$biomePacket.addBlockPos(convertedPos);
        return false;
    }

    @Inject(
            method = "killBiome",
            at = @At(value = "TAIL"),
            remap = false
    )
    private static void srpmixins_sendBiomePacket_kill(World worldIn, BlockPos pos, int age, CallbackInfo ci){
        if(srpmixins$biomePacket != null) {
            SRPMain.network.sendToDimension(srpmixins$biomePacket, worldIn.provider.getDimension());
            srpmixins$biomePacket = null;
        }
    }

    @WrapOperation(
            method = "killBiome",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;nearestHeartAge(Lnet/minecraft/util/math/BlockPos;ZI)I"),
            remap = false
    )
    private static int srpmixins_removeUnusedCall_nearestHeartAge(SRPWorldData instance, BlockPos ageDistance, boolean currentage, int i, Operation<Integer> original){
        return 0;
    }

    @WrapOperation(
            method = "killBiome",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getDistanceSpreadByAge(IZ)I"),
            remap = false
    )
    private static int srpmixins_removeUnusedCall_getDistanceSpreadByAge(SRPWorldData instance, int age, boolean sq, Operation<Integer> original){
        return 0;
    }
}
