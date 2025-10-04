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