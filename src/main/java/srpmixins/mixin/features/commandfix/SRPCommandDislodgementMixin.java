package srpmixins.mixin.features.commandfix;

import com.dhanantry.scapeandrunparasites.network.SRPCommandDislodgment;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(SRPCommandDislodgment.class)
public abstract class SRPCommandDislodgementMixin implements ICommand {
    /**
     * @author Nischhelm
     * @reason incorrectly overridden
     */
    @Overwrite(remap = false)
    public int compareTo(ICommand arg0) {
        return this.getName().compareTo(arg0.getName());
    }
}
