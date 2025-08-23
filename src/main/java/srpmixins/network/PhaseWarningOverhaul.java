package srpmixins.network;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMovingSound;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.SRPReference;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.UUID;

public class PhaseWarningOverhaul {

    public static void alertOnePlayer(World worldIn, UUID playerUUID, String message, int warning) {
        boolean sendToAll = !SRPMixinsConfigHandler.playerphases.enabled || playerUUID == null;

        if(sendToAll) ParasiteEventEntity.alertAllPlayerDim(worldIn, message, warning);
        else {
            EntityPlayer player = worldIn.getPlayerEntityByUUID(playerUUID);
            if (player == null) return;
            player.sendMessage(new TextComponentString(message));
            SRPMain.network.sendTo(new SRPPacketMovingSound(warning), (EntityPlayerMP) player);

            if (warning == -7 && message.equals("Phase decreased"))
                for (EntityParasiteBase entity : worldIn.getEntities(EntityParasiteBase.class, ent -> ent.getDistanceSq(player) <= 65536))
                    entity.addPotionEffect(new PotionEffect(SRPPotions.RAGE_E, 2400, 1, false, false));
        }
    }

    public static void sendUnlockMessage(World world, UUID playerUUID, String message, int paraId){
        boolean sendToAll = !SRPMixinsConfigHandler.playerphases.enabled || playerUUID == null;

        ITextComponent msg;
        if(message.contains("%")) {
            ResourceLocation loc = new ResourceLocation(SRPReference.MOD_ID, SRPMobConfigProvider.paraIdToMobName.get(paraId));
            msg = new TextComponentTranslation(message, new TextComponentTranslation("entity." + EntityList.getTranslationName(loc) + ".name"));
        } else
            msg = new TextComponentString(message);

        if(!sendToAll) {
            EntityPlayer player = world.getPlayerEntityByUUID(playerUUID);
            if(player != null) player.sendMessage(msg);
        } else
            world.getMinecraftServer().getPlayerList().getPlayers().forEach(player -> player.sendMessage(msg));
    }
}
