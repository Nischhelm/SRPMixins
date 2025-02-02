package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.item.ItemEPClock;
import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.handlers.SRPMixinsConfigHandler;
import srpmixins.util.SRPSaveDataInterface;

import static java.lang.Math.min;
import static java.lang.Math.round;

@Mixin(ItemEPClock.class)
public abstract class ModifiedBloodyClock {

    @Unique
    private SRPSaveData saveData;

    @Inject(method="func_77659_a",
            at = @At(value="HEAD"),
            remap = false
    )
    public void saveWorldDataMixin(World worldIn, EntityPlayer playerIn, EnumHand handIn, CallbackInfoReturnable<ActionResult<ItemStack>> cir){
        if(!worldIn.isRemote) {
            saveData = SRPSaveData.get(worldIn);
            if (SRPMixinsConfigHandler.phasepoints.playerPhases)
                saveData = ((SRPSaveDataInterface) saveData).getByPlayer(worldIn, playerIn.getUniqueID());
        }
    }

    @Redirect(
            method = "func_77659_a",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;func_146105_b(Lnet/minecraft/util/text/ITextComponent;Z)V"),
            remap = false
    )
    public void sendPhaseMessageToChatMixin(EntityPlayer player, ITextComponent iTextComponent, boolean b) {
        if (SRPMixinsConfigHandler.phasepoints.modifyBloodyClock) {
            int dimension = player.getEntityWorld().provider.getDimension();
            byte evoPhase = saveData.getEvolutionPhase(dimension);
            int pointsNext = SRPCommandEvolution.getNeededPoints((byte) min(evoPhase + 1, 8));
            int pointsThis = SRPCommandEvolution.getNeededPoints(evoPhase);
            int perc = (int) round((100. * ((double) saveData.getTotalKills(dimension) - pointsThis)) / ((double) pointsNext - pointsThis));
            if (pointsNext == pointsThis) perc = 0;
            int cooldown = saveData.getCooldown(player.getEntityWorld(), dimension);
            if (SRPMixinsConfigHandler.phasepoints.bloodyClockShowsCooldown && cooldown > 0) {
                int hours = cooldown / 3600;    cooldown -= 3600 * hours;
                int minutes = (cooldown) / 60;  cooldown -= 60 * minutes;
                int seconds = cooldown;

                ITextComponent cooldownMsg;
                if (hours > 0) cooldownMsg = new TextComponentTranslation("socketed.bloodyclock.hours", hours, minutes, seconds);
                else if (minutes > 0) cooldownMsg = new TextComponentTranslation("socketed.bloodyclock.minutes", minutes, seconds);
                else cooldownMsg = new TextComponentTranslation("socketed.bloodyclock.seconds", seconds);

                player.sendStatusMessage(new TextComponentTranslation("srpmixins.bloodyclock.phaseandcooldown", Integer.toString(evoPhase), perc, cooldownMsg), true);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("srpmixins.bloodyclock.currentphase", Integer.toString(evoPhase), perc), true);
            }
        } else
            player.sendStatusMessage(iTextComponent, b);
    }
}