package srpmixins.mixin.phaseresetfix.srpworlddata;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPWorldData.class)
public abstract class SRPWorldDataMixin {
    @Shadow(remap = false) private static SRPWorldData instance;
    @Shadow(remap = false) private static SRPWorldData create(World world, MapStorage storage) { return null; }

    //This data is just a default SRPWorldData object that tries to protect the server from crashing.
    // if it was just me, i would just return null and uncover all phase reset bugs but lets rather protect ppls mental health
    @Unique private static SRPWorldData srpmixins$clientSRPData = null;
    @Unique private static long srpmixins$lastMsgTick = 0;

    @Inject(
            method = "get",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_fullyDisableNodeResetsForever(World world, CallbackInfoReturnable<SRPWorldData> cir) {
        //SRP allows clientside read of SRPWorldData, which due to the data setup will overwrite the serverside data on singleplayer
        //This disables it
        if (!world.isRemote) return; //Serverside calls are fine

        //Initialise a default clientside SRPWorldData instance the first time this happens
        if (srpmixins$clientSRPData == null) {
            SRPWorldData tmpInstance = instance;

            instance = new SRPWorldData("SRPMixins_protects_your_nodes");
            srpmixins$clientSRPData = create(world, null); //createData writes on instance, so we had to tmp save it

            instance = tmpInstance;
        }

        //Send log + msg
        long currTime = world.getTotalWorldTime();
        if (SRPMixinsConfigHandler.phasepoints.phaseResetDebugMode && (currTime - srpmixins$lastMsgTick > 100)) {
            srpmixins$lastMsgTick = currTime;
            try {
                throw (new Exception("SRP tried to reset your node data"));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                for (EntityPlayer player : world.playerEntities) {
                    if (player != null)
                        player.sendMessage(new TextComponentString("SRPMixins prevented an accidental node reset. Check (serverside) latest.log and send to Nischhelm").setStyle(new Style().setColor(TextFormatting.RED)));
                }
            }
        }

        cir.setReturnValue(srpmixins$clientSRPData);
    }
}