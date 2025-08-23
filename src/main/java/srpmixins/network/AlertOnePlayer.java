package srpmixins.network;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMovingSound;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.UUID;

public class AlertOnePlayer {

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
}
