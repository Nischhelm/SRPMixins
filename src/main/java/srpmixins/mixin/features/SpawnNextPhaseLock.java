package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(value = ParasiteEventEntity.class, priority = 1001)
public abstract class SpawnNextPhaseLock {
    @WrapMethod(
            method = "spawnNext",
            remap = false
    )
    private static void srpmixins_spawnNextPhaseLock(EntityParasiteBase entityIn, EntityParasiteBase entityOut, boolean effects, boolean thunder, Operation<Void> original){
        if(entityIn.world.isRemote || !SRPConfigSystems.useEvolution) { original.call(entityIn, entityOut, effects, thunder); return; }

        String mobIn = srpmixins$getNameForEntity(entityIn);
        String mobOut = srpmixins$getNameForEntity(entityOut);
        if(mobIn.isEmpty() || mobOut.isEmpty()) { original.call(entityIn, entityOut, effects, thunder); return; }

        if(!srpmixins$conversionIsPhaseLocked(entityIn, mobIn, mobOut))
            original.call(entityIn, entityOut, effects, thunder);
        //else no conversion
    }

    @WrapOperation(
            method = "convertEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityList;createEntityByIDFromName(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;")
    )
    private static Entity srpmixins_conversionPhaseLock(ResourceLocation locOut, World worldIn, Operation<Entity> original, @Local(argsOnly = true) EntityLivingBase entityIn, @Local(ordinal = 0) String mobIn, @Cancellable CallbackInfo ci){
        if(entityIn.world.isRemote || !SRPConfigSystems.useEvolution) { return original.call(locOut, worldIn); }
        if(locOut == null) return null;

        mobIn = mobIn.replace("srparasites:","");
        String mobOut = locOut.toString().replace("srparasites:","");

        if(srpmixins$conversionIsPhaseLocked(entityIn, mobIn, mobOut)){
            ci.cancel(); //Don't convert
            return null;
        }

        return original.call(locOut, worldIn);
    }

    @WrapOperation(
            method = {"convertEntityFeral","hijackEntity"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityList;createEntityByIDFromName(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;")
    )
    private static Entity srpmixins_conversionPhaseLockFeralAndHijack(ResourceLocation locOut, World worldIn, Operation<Entity> original, @Local(argsOnly = true) EntityLivingBase entityIn, @Local(ordinal = 0) String mobIn, @Cancellable CallbackInfoReturnable<Boolean> cir){
        if(entityIn.world.isRemote || !SRPConfigSystems.useEvolution) { return original.call(locOut, worldIn); }
        if(locOut == null) return null;

        mobIn = mobIn.replace("srparasites:","");
        String mobOut = locOut.toString().replace("srparasites:","");

        if(srpmixins$conversionIsPhaseLocked(entityIn, mobIn, mobOut)){
            cir.setReturnValue(false); //Don't convert
            return null;
        }

        return original.call(locOut, worldIn);
    }

    @WrapOperation(
            method = "merge",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_mergePhaseLock(String mergeListEntry, String sep, Operation<String[]> original, @Local(argsOnly = true) EntityParasiteBase entityIn, @Cancellable CallbackInfoReturnable<Boolean> cir){
        if(entityIn.world.isRemote || !SRPConfigSystems.useEvolution) { return original.call(mergeListEntry, sep); }
        ResourceLocation locIn = EntityList.getKey(entityIn);
        if(locIn == null) { return original.call(mergeListEntry, sep); }

        String mobIn = srpmixins$getNameForEntity(entityIn);
        //Not using original.call here since it's returning an empty list
        String mobOut = mergeListEntry.substring(0, mergeListEntry.indexOf(";")).replace("srparasites:","");

        if(srpmixins$conversionIsPhaseLocked(entityIn, mobIn, mobOut)){
            cir.setReturnValue(false); //Don't convert
            return null;
        }

        return original.call(mergeListEntry, sep);
    }

    @Unique
    private static String srpmixins$getNameForEntity(Entity entity){
        if(entity instanceof EntityParasiteBase){
            int paraId = ((EntityParasiteBase) entity).getParasiteIDRegister();
            return SRPMixinsConfigProvider.paraIdToMobName.getOrDefault(paraId,"");
        } else {
            ResourceLocation locIn = EntityList.getKey(entity);
            if(locIn != null) return locIn.toString();
            else return "";
        }
    }

    @Unique
    private static boolean srpmixins$conversionIsPhaseLocked(Entity entityIn, String mobIn, String mobOut){
        int currPhase = SRPSaveDataInterface.get(entityIn.world, null, entityIn.getPosition()).getEvolutionPhase(entityIn.world.provider.getDimension());
        int phaseLock = SRPMixinsConfigProvider.getConversionPhaseLock(mobIn, mobOut);
        return currPhase < phaseLock;
    }
}
