package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.item.ItemEPClock;
import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPSaveDataInterface;

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
            if (SRPMultiplierConfigHandler.phasepoints.playerPhases)
                saveData = ((SRPSaveDataInterface) saveData).getByPlayer(worldIn, playerIn.getUniqueID());
        }
    }

    @Redirect(
            method = "func_77659_a",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;func_146105_b(Lnet/minecraft/util/text/ITextComponent;Z)V"),
            remap = false
    )
    public void sendPhaseMessageToChatMixin(EntityPlayer player, ITextComponent iTextComponent, boolean b) {
        if (SRPMultiplierConfigHandler.phasepoints.modifyBloodyClock) {
            int dimension = player.getEntityWorld().provider.getDimension();
            byte evoPhase = saveData.getEvolutionPhase(dimension);
            int pointsNext = SRPCommandEvolution.getNeededPoints((byte) min(evoPhase + 1, 8));
            int pointsThis = SRPCommandEvolution.getNeededPoints(evoPhase);
            int perc = (int) round((100. * ((double) saveData.getTotalKills(dimension) - pointsThis)) / ((double) pointsNext - pointsThis));
            if (pointsNext == pointsThis) perc = 0;
            player.sendStatusMessage(new TextComponentString("Current Phase: "+Integer.toString(evoPhase)).appendText(" (" + perc + "%)"), true);
        } else
            player.sendStatusMessage(iTextComponent, b);
    }
}