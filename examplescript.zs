import srpmixins.BlockInfestationEvent as BlockInfestationEvent;
import srpmixins.SRPSaveData as SRPSaveData;

//Various things to do with block infestation
events.onBlockInfestation(function(event as BlockInfestationEvent) {
    val data = SRPSaveData.get(event.world) as SRPSaveData;

    //Turn infesting(!) block into obsidian
    if(event.world.getBiome(event.position).id == "minecraft:plains"){
        if(isNull(event.facing)) return;
            event.world.setBlockState(<blockstate:minecraft:obsidian>, event.position.getOffset(event.facing.opposite,1));
            event.cancelFully(); //cancelFully is more performant than cancel if we turn the original block into smth
    }

    //No infestation in phase 8
    else if(data.phase == 8)
        event.cancel();

    //turn infested block into air when cooldown is 0
    else if(data.cooldown == 0)
        event.resultState = <blockstate:minecraft:air>;

    //increase points by 5000 if points are below the ones needed for phase 5 (basically the same as asking for phase < 5)
    else if(data.points < SRPSaveData.getPointThreshold(5))
        data.points += 5000;
});

//Remove COTH when taking fire dmg (20% chance)
val removalChance = 0.2;
events.onEntityLivingHurt(function(event as crafttweaker.event.EntityLivingHurtEvent){
    if(!event.damageSource.fireDamage) return;
    if(!event.entityLivingBase.isPotionActive(<potion:srparasites:coth>)) return;
    if(event.entity.world.random.nextFloat() < removalChance)
        event.entityLivingBase.removePotionEffect(<potion:srparasites:coth>);
});





//----------- the following in a separate script ------------

//create a block that when rigthclicked increases points by 100000, needs contenttweaker

#loader contenttweaker

import srpmixins.SRPSaveData as SRPSaveData;
import mods.contenttweaker.VanillaFactory as VanillaFactory;
import mods.contenttweaker.Block as Block;

var zsBlock = VanillaFactory.createBlock("parasiting_block", <blockmaterial:rock>) as Block;
zsBlock.register();

events.onPlayerInteractBlock(function(event as crafttweaker.event.PlayerInteractBlockEvent){
    if(event.world.remote) return;
    if(event.useBlock == "DENY") return;
    if(event.useItem == "ALLOW") return;
    if(event.block.definition.id == "contenttweaker:parasiting_block"){
        event.useBlock = "ALLOW";
        event.useItem = "DENY";
        val data = SRPSaveData.get(event.world);
        data.points += 100000;
        event.world.setBlockState(<blockstate:minecraft:air>, event.position);
        event.player.sendStatusMessage("Parasiting yourself!");
    }
});