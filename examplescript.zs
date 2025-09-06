import srpmixins.BlockInfestationEvent as BlockInfestationEvent;
import srpmixins.SRPSaveData as SRPSaveData;

events.onBlockInfestation(function(event as BlockInfestationEvent) {
    val data = SRPSaveData.get(event.world) as SRPSaveData;
    if(event.world.getBiome(event.position).id == "minecraft:plains")
        event.cancel();
    if(data.phase == 8)
        event.cancel();
    else if(data.cooldown == 0)
        event.resultState = <blockstate:minecraft:air>;
    else if(data.points < SRPSaveData.getPointThreshold(5))
        data.points += 5000;
});