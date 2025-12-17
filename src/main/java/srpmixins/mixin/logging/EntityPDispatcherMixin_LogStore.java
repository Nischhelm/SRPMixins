package srpmixins.mixin.logging;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPDispatcher;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.SRPMixins;

@Mixin(value = EntityPDispatcher.class, remap = false)
public abstract class EntityPDispatcherMixin_LogStore extends Entity {

    @Shadow protected int crude;
    @Shadow protected int inf;
    @Shadow protected int mangler;
    @Shadow protected int flesh;

    public EntityPDispatcherMixin_LogStore(World worldIn) { super(worldIn); }

    @Unique
    private void srpmixins$logStore(String funnel, EntityParasiteBase in) {
        if (this.world.isRemote) return;
        String key = in == null ? "null" : String.valueOf(EntityList.getKey(in));
        SRPMixins.logInWorld(this.world, String.format("[DispatcherStore] id=%d funnel=%s mob=%s crude=%d inf=%d mangler=%d flesh=%d",
                this.getEntityId(), funnel, key, crude, inf, mangler, flesh));
    }

    @Inject(method = "storeLodo", at = @At("RETURN"))
    private void srpmixins$logStoreLodo(EntityParasiteBase in, boolean upgrade, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) srpmixins$logStore("Lodo", in);
    }

    @Inject(method = "storeInf", at = @At("RETURN"))
    private void srpmixins$logStoreInf(EntityParasiteBase in, boolean upgrade, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) srpmixins$logStore("Inf", in);
    }

    @Inject(method = "storeCrude", at = @At("RETURN"))
    private void srpmixins$logStoreCrude(EntityParasiteBase in, boolean upgrade, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) srpmixins$logStore("Crude", in);
    }

    @Inject(method = "storeMudo", at = @At("RETURN"))
    private void srpmixins$logStoreMudo(EntityParasiteBase in, boolean upgrade, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) srpmixins$logStore("Mudo", in);
    }

    @Inject(method = "storeMangler", at = @At("RETURN"))
    private void srpmixins$logStoreMangler(EntityParasiteBase in, boolean upgrade, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) srpmixins$logStore("Mangler", in);
    }

    @Inject(method = "storeAll", at = @At("RETURN"))
    private void srpmixins$logStoreAll(EntityParasiteBase in, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) srpmixins$logStore("All", in);
    }

    @Inject(method = "addPrim", at = @At("TAIL"))
    private void srpmixins$logAddPrim(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[DispatcherPayout] id=%d +Prim crude=%d inf=%d mangler=%d flesh=%d",
                this.getEntityId(), crude, inf, mangler, flesh));
    }
}
