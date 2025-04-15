package srpmixins.util.customphasemechanics;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.UUID;

public class PlayerPhases_AlertOnePlayer {

    public static void alertOnePlayer(World worldIn, UUID playerUUID, String message, int warning) {
        EntityPlayer player = worldIn.getPlayerEntityByUUID(playerUUID);
        if(player!=null){
            player.sendMessage(new TextComponentString(message));
            SRPMain.network.sendTo(new SRPPacketMovingSound(warning),(EntityPlayerMP) player);

            if (warning == -7 && message.equals("Phase decreased"))
                for (Entity entity : worldIn.loadedEntityList)
                    if (entity instanceof EntityParasiteBase && entity.getDistanceSq(player)<=256*256)
                        ((EntityParasiteBase) entity).addPotionEffect(new PotionEffect(SRPPotions.RAGE_E, 2400, 1, false, false));
        }
    }
}