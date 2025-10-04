import crafttweaker.entity.IEntityItem;
import crafttweaker.player.IPlayer;

val lurecomponents = [
    "item.srparasites.lurecomponent1",
    "item.srparasites.lurecomponent2",
    "item.srparasites.lurecomponent3",
    "item.srparasites.lurecomponent4",
    "item.srparasites.lurecomponent5",
    "item.srparasites.lurecomponent6"
] as string[];

val craftingingredients = [
    //"item.srparasites.assimilated_flesh", //don't reduce infected flesh drops
    "item.srparasites.ada_summoner_drop",
    "item.srparasites.ada_yelloweye_drop",
    "item.srparasites.ada_manducater_drop",
    "item.srparasites.ada_reeker_drop",
    "item.srparasites.ada_longarms_drop",
    "item.srparasites.ada_bolster_drop",
    "item.srparasites.ada_arachnida_drop",
    "item.srparasites.ada_devourer_drop",
    "item.srparasites.beckon_drop",
    "item.srparasites.dispatcher_drop",
    "item.srparasites.bone"
] as string[];

val bloodtear = "item.contenttweaker.blood_tear";

events.onEntityLivingDeathDrops(function(event as crafttweaker.event.EntityLivingDeathDropsEvent) {
    if(event.entity instanceof IPlayer) return;
    if(!(event.entity.definition.name has "srparasites.")) return; //only parasite drops
    val dimId = event.entity.dimension;
    if(dimId == 111) return; //no change in LC
    val isOverworld = dimId == 0;

    val craftingIngredientChance = isOverworld ? 0.25 : 0.5;
    val rng = event.entity.world.random;

    var itemsToKeep as IEntityItem[] = [];
    for  item in event.drops {
        val dropName = item.item.definition.name;
        if(lurecomponents has dropName) itemsToKeep += item; //keep lures
        else if(craftingingredients has dropName || (bloodtear == dropName && !isOverworld)){
            if(rng.nextFloat() < craftingIngredientChance) {
                itemsToKeep += item; //keep
            }
        }
        else if(bloodtear != dropName) {
            itemsToKeep += item; //remove blood tears entirely in overworld, keep anything else
        }
    }
    event.drops = itemsToKeep;
});