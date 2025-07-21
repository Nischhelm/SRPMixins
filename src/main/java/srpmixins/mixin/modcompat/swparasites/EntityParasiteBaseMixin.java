package srpmixins.mixin.modcompat.swparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.existingeevee.swparasites.init.ParasiteSWProperties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.oblivioussp.spartanweaponry.api.IWeaponPropertyContainer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin extends EntityMob {
    public EntityParasiteBaseMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", ordinal = 0)
    )
    private Item srpmixins_uncappedSWPItems(
            Item original,
            @Cancellable CallbackInfoReturnable<Boolean> cir,
            @Local(argsOnly = true) DamageSource source,
            @Local(argsOnly = true) float amount
    ) {
        if (original instanceof IWeaponPropertyContainer) {
            IWeaponPropertyContainer<?> container = (IWeaponPropertyContainer<?>) original;
            if (container.hasWeaponProperty(ParasiteSWProperties.UNCAPPED))
                cir.setReturnValue(super.attackEntityFrom(source, amount));
        }
        return original;
    }
}
