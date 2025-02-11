package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.item.ItemEPClock;
import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigHandler;

import static java.lang.Math.min;
import static java.lang.Math.round;

@Mixin(ItemEPClock.class)
public abstract class ModifiedBloodyClock {
    @Redirect(
            method = "onItemRightClick",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;sendStatusMessage(Lnet/minecraft/util/text/ITextComponent;Z)V")
    )
    public void sendPhaseMessageToChatMixin(EntityPlayer player, ITextComponent iTextComponent, boolean b, @Local(argsOnly = true) World worldIn, @Local SRPSaveData saveData) {
        if (SRPMixinsConfigHandler.phasepoints.modifyBloodyClock) {
            int dimension = worldIn.provider.getDimension();
            byte evoPhase = saveData.getEvolutionPhase(dimension);
            if(evoPhase == -2) {
                player.sendStatusMessage(new TextComponentTranslation("srpmixins.bloodyclock.phaseminustwo"), true);
                return;
            } else if(evoPhase == -1){
                player.sendStatusMessage(new TextComponentTranslation("srpmixins.bloodyclock.phaseminusone"), true);
                return;
            }

            int pointsNext = SRPCommandEvolution.getNeededPoints((byte) min(evoPhase + 1, 10));
            int pointsThis = SRPCommandEvolution.getNeededPoints(evoPhase);
            int perc = (int) round((100. * ((double) saveData.getTotalKills(dimension) - pointsThis)) / ((double) pointsNext - pointsThis));
            if (pointsNext == pointsThis) perc = 0;

            int cooldown = saveData.getCooldown(worldIn, dimension);

            if (SRPMixinsConfigHandler.phasepoints.bloodyClockShowsCooldown && cooldown > 0) {
                int hours = cooldown / 3600;    cooldown -= 3600 * hours;
                int minutes = (cooldown) / 60;  cooldown -= 60 * minutes;
                int seconds = cooldown;

                ITextComponent cooldownMsg;
                if (hours > 0) cooldownMsg = new TextComponentTranslation("srpmixins.bloodyclock.hours", hours, minutes, seconds);
                else if (minutes > 0) cooldownMsg = new TextComponentTranslation("srpmixins.bloodyclock.minutes", minutes, seconds);
                else cooldownMsg = new TextComponentTranslation("srpmixins.bloodyclock.seconds", seconds);

                player.sendStatusMessage(new TextComponentTranslation("srpmixins.bloodyclock.phaseandcooldown", Integer.toString(evoPhase), perc, cooldownMsg), true);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("srpmixins.bloodyclock.currentphase", Integer.toString(evoPhase), perc), true);
            }
        } else
            player.sendStatusMessage(iTextComponent, b);
    }
}