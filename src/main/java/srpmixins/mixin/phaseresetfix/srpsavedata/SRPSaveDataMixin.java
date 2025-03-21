package srpmixins.mixin.phaseresetfix.srpsavedata;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

import java.io.PrintStream;

@Mixin(SRPSaveData.class)
public abstract class SRPSaveDataMixin {
    @Shadow(remap = false) private static SRPSaveData instance;
    @Shadow(remap = false) private static SRPSaveData createData(World world, MapStorage storage) { return null; }

    //This data is just a default SRPSaveData object that tries to protect the server from crashing.
    // if it was just me, i would just return null and uncover all phase reset bugs but lets rather protect ppls mental health
    @Unique private static SRPSaveData srpmixins$clientSRPData = null;
    @Unique private static long srpmixins$lastMsgTick = 0;

    @Inject(
            method = "get",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_fullyDisablePhaseResetsForever(World world, CallbackInfoReturnable<SRPSaveData> cir) {
        //SRP allows clientside read of SRPSaveData, which due to the data setup will overwrite the serverside data on singleplayer
        //This disables it
        if (!world.isRemote) return; //Serverside calls are fine

        //Initialise a default clientside SRPSaveData instance the first time this happens
        if (srpmixins$clientSRPData == null) {
            SRPSaveData tmpInstance = instance;

            instance = new SRPSaveData("SRPMixins_protects_your_phase");
            srpmixins$clientSRPData = createData(world, null); //createData writes on instance, so we had to tmp save it

            instance = tmpInstance;
        }

        //Send log + msg
        long currTime = world.getTotalWorldTime();
        if (SRPMixinsConfigHandler.phasepoints.phaseResetDebugMode && (currTime - srpmixins$lastMsgTick > 100)) {
            srpmixins$lastMsgTick = currTime;
            try {
                throw (new Exception("SRP tried to reset your phase"));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                for (EntityPlayer player : world.playerEntities) {
                    if (player != null)
                        player.sendMessage(new TextComponentString("SRPMixins prevented an accidental phase reset. Check (serverside) latest.log and send to Nischhelm").setStyle(new Style().setColor(TextFormatting.RED)));
                }
            }
        }

        cir.setReturnValue(srpmixins$clientSRPData);
    }

    //Just a cancel for SRPs "nanissss--------asdddddddddddddddddddddddddddddddddddddddd----" debug msg
    @Redirect(
            method = "get",
            at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"),
            remap = false
    )
    private static void srpmixins_disableSRPDebugMessage(PrintStream instance, String s) {
        //no op
    }
}