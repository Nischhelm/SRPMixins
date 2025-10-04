#loader contenttweaker

import srpmixins.SRPSaveData as SRPSaveData;
import mods.contenttweaker.VanillaFactory as VanillaFactory;
import mods.contenttweaker.Block as Block;

//create a block that when rigthclicked increases points by 100000, needs contenttweaker

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