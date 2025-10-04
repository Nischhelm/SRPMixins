//Remove COTH when taking fire dmg (20% chance)
val removalChance = 0.2;
events.onEntityLivingHurt(function(event as crafttweaker.event.EntityLivingHurtEvent){
    if(!event.damageSource.fireDamage) return;
    if(!event.entityLivingBase.isPotionActive(<potion:srparasites:coth>)) return;
    if(event.entity.world.random.nextFloat() < removalChance)
        event.entityLivingBase.removePotionEffect(<potion:srparasites:coth>);
});
