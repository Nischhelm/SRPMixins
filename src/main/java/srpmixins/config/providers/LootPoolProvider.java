package srpmixins.config.providers;

import com.dhanantry.scapeandrunparasites.util.SRPReference;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class LootPoolProvider {
    public static final Map<Integer, ResourceLocation> lootTableLocations = new HashMap<>();
    public static final Map<ResourceLocation, LootTable> lootTables = new HashMap<>();
    private static File lootTablesFolder;

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event){
        if(!event.getName().getNamespace().equals(SRPReference.MOD_ID)) return;
        event.setTable(lootTables.get(event.getName()));
    }

    public static void setupLootPoolFolders(File modConfig){
        File modFolder = new File(modConfig, SRPMixins.MODID);
        if(!modFolder.exists() || !modFolder.isDirectory()) {
            if(!modFolder.mkdir()) {
                SRPMixins.LOGGER.error("SRPMixins: Could not create general configuration folder");
                return;
            }
        }

        lootTablesFolder = new File(modFolder, "loot_tables");
        if(!lootTablesFolder.exists() || !lootTablesFolder.isDirectory()) {
            if(!lootTablesFolder.mkdir()) {
                SRPMixins.LOGGER.error("SRPMixins: Could not create loot table configuration folder");
            }
        }
    }

    public static final LootCondition[] noConditions = new LootCondition[0];
    public static void parseLootPools() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
                .registerTypeAdapter(LootPool.class, new LootPool.Serializer())
                .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
                .registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer())
                .registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer())
                .registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer())
                .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
                .setPrettyPrinting().create();

        Map<Integer, String[]> lootTableConfigs = new HashMap<Integer, String[]>(){{
            put(1, SRPConfigMobs.shycoLoot);
            put(2, SRPConfigMobs.dorpaLoot);
            put(3, SRPConfigMobs.ratholLoot);
            put(4, SRPConfigMobs.emanaLoot);
            put(5, SRPConfigMobs.LodoLoot);
            put(6, SRPConfigMobs.infhumanLoot);
            put(7, SRPConfigMobs.hullLoot);
            put(8, SRPConfigMobs.canraLoot);
            put(9, SRPConfigMobs.alafhaLoot);
            put(10, SRPConfigMobs.noglaLoot);
            put(11, SRPConfigMobs.butholLoot);
            put(12, SRPConfigMobs.mudoLoot);
            put(13, SRPConfigMobs.infcowLoot);
            put(14, SRPConfigMobs.infsheepLoot);
            put(15, SRPConfigMobs.infwolfLoot);
            put(16, SRPConfigMobs.venkrolLoot);
            put(17, SRPConfigMobs.zetmoLoot);
            put(18, SRPConfigMobs.venkrolsiiLoot);
            put(19, SRPConfigMobs.venkrolsiiiLoot);
            put(20, SRPConfigMobs.terlaLoot);
            put(21, SRPConfigMobs.infwolfheadLoot);
            put(22, SRPConfigMobs.infsheepheadLoot);
            put(23, SRPConfigMobs.kolLoot);
            put(24, SRPConfigMobs.oroncoLoot);
            put(25, SRPConfigMobs.angedLoot);
            put(26, SRPConfigMobs.infpigLoot);
            put(27, SRPConfigMobs.infvillagerLoot);
            put(28, SRPConfigMobs.infcowheadLoot);
            put(29, SRPConfigMobs.tonroLoot);
            put(30, SRPConfigMobs.unvoLoot);
            put(31, SRPConfigMobs.infpigheadLoot);
            put(32, SRPConfigMobs.infvillagerheadLoot);
            put(33, SRPConfigMobs.ganroLoot);
            put(34, SRPConfigMobs.pod1Loot);
            put(36, SRPConfigMobs.kolLoot);
            put(37, SRPConfigMobs.wymoLoot);
            put(38, SRPConfigMobs.arachnidaLoot);
            put(39, SRPConfigMobs.inhooSLoot);
            put(40, SRPConfigMobs.infadventurerLoot);
            put(41, SRPConfigMobs.venkrolsivLoot);
            put(43, SRPConfigMobs.inhooMLoot);
            put(44, SRPConfigMobs.infhorseLoot);
            put(45, SRPConfigMobs.infhorseheadLoot);
            put(46, SRPConfigMobs.infhumanheadLoot);
            put(47, SRPConfigMobs.ombooLoot);
            put(48, SRPConfigMobs.hostLoot);
            put(49, SRPConfigMobs.infbearLoot);
            put(50, SRPConfigMobs.esorLoot);
            put(51, SRPConfigMobs.shycoadaptedloot);
            put(52, SRPConfigMobs.hulladaptedloot);
            put(53, SRPConfigMobs.canraadaptedloot);
            put(54, SRPConfigMobs.noglaadaptedloot);
            put(55, SRPConfigMobs.emanaadaptedloot);
            put(56, SRPConfigMobs.zetmoadaptedloot);
            //put(57, SRPConfigMobs.shycoLoot); //idk what mob thats supposed to be
            put(58, SRPConfigMobs.arachnidaadaptedloot);
            put(59, SRPConfigMobs.infendermanLoot);
            put(60, SRPConfigMobs.flogLoot);
            put(62, SRPConfigMobs.cruxaLoot);
            put(63, SRPConfigMobs.heedLoot);
            put(64, SRPConfigMobs.infdragoneLoot);
            put(65, SRPConfigMobs.jinjoLoot);
            put(66, SRPConfigMobs.lumLoot);
            put(69, SRPConfigMobs.infendermanheadLoot);
            put(70, SRPConfigMobs.infdragoneheadLoot);
            put(71, SRPConfigMobs.infadventurerheadLoot);
            put(72, SRPConfigMobs.nakLoot);
            put(73, SRPConfigMobs.dodsiLoot);
            put(74, SRPConfigMobs.ratholLoot);
            put(75, SRPConfigMobs.herdLoot);
            put(76, SRPConfigMobs.nuuhLoot);
            put(77, SRPConfigMobs.dodsiiLoot);
            put(78, SRPConfigMobs.dodsiiiLoot);
            put(79, SRPConfigMobs.dodsivLoot);
            put(80, SRPConfigMobs.thrallLoot);
            //put(81, SRPConfigMobs.lumadaptedloot); //mob doesnt exist yet
            put(82, SRPConfigMobs.ombooLoot);
            put(84, SRPConfigMobs.orchLoot);
            put(85, SRPConfigMobs.elviaLoot);
            put(86, SRPConfigMobs.lenciaLoot);
            put(87, SRPConfigMobs.pheonLoot);
            put(88, SRPConfigMobs.vestaLoot);
            put(89, SRPConfigMobs.flamLoot);
            put(91, SRPConfigMobs.ataLoot);
            put(92, SRPConfigMobs.ikiLoot);
            put(93, SRPConfigMobs.fercowLoot);
            put(94, SRPConfigMobs.ferendermanLoot);
            put(95, SRPConfigMobs.ferhorseLoot);
            put(96, SRPConfigMobs.ferhumanLoot);
            put(97, SRPConfigMobs.ferpigLoot);
            put(98, SRPConfigMobs.fersheepLoot);
            put(99, SRPConfigMobs.fervillagerLoot);
            put(300, SRPConfigMobs.ferwolfLoot);
            put(301, SRPConfigMobs.higolemLoot);
            put(304, SRPConfigMobs.gotholLoot);
            put(306, SRPConfigMobs.ferbearLoot);
            put(307, SRPConfigMobs.infsquidLoot);
            put(309, SRPConfigMobs.hebluLoot);
        }};

        for (Map.Entry<Integer, String[]> entry : lootTableConfigs.entrySet()) {
            LootTable mobLoot = new LootTable(new LootPool[0]);
            LootPool pool_dropOnlyOne = getLootPool(null, "only_one");
            boolean hasDropOnlyOneEntry = false;

            String paraName = SRPMobConfigProvider.paraIdToMobName.get(entry.getKey());
            if(paraName == null){
                SRPMixins.LOGGER.error("SRPMixins unable to find parasite with id {}", entry.getKey());
                continue;
            }

            for (String s : entry.getValue()) {
                String[] split = s.split(";");
                if(split.length < 4){
                    SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line for {}, too few entries (Expected pattern: modid:itemname;oddsIn100;maxQuantity;alwaysDrop): {}", paraName, s);
                    continue;
                }
                Item item = Item.getByNameOrId(split[0].trim());

                if (item == null) SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line for {}, item {} doesn't exist: {}", paraName, split[0], s);
                else {
                    try {
                        int chance = Integer.parseInt(split[1].trim());
                        int maxQuantity = Integer.parseInt(split[2].trim());
                        boolean independentDrop = Boolean.parseBoolean(split[3].trim());

                        if (independentDrop)
                            mobLoot.addPool(getLootPool(getLootEntry(item, maxQuantity, chance), "independent pool for " + item.getRegistryName().toString()));
                        else{
                            pool_dropOnlyOne.addEntry(getLootEntry(item, maxQuantity, chance));
                            hasDropOnlyOneEntry = true;
                        }
                    } catch (Exception e) {
                        SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line for {}, expected String; int; int; boolean: {}", paraName, s);
                    }
                }
            }

            if(hasDropOnlyOneEntry) mobLoot.addPool(pool_dropOnlyOne);
            ResourceLocation lootTableLoc = new ResourceLocation(SRPReference.MOD_ID, "entities/"+paraName);
            lootTableLocations.put(entry.getKey(), lootTableLoc);
            lootTables.put(lootTableLoc, mobLoot);
            LootTableList.register(lootTableLoc); //register the pool

            //Write to mods/srpmixins/loot_tables/para_name.json
            try {
                File file = new File(lootTablesFolder, String.format("%s.json", paraName));
                if(file.exists()) continue; //don't write if the file already exists
                else if(!file.createNewFile()) SRPMixins.LOGGER.error("SRPMixins: Failed to create new loot table file, {}", paraName);
                else if(!file.setWritable(true)) SRPMixins.LOGGER.error("SRPMixins: Failed to set new loot table file writeable, {}", paraName);
                else {
                    JsonObject elem = cleanUpJson(gson.toJsonTree(mobLoot).getAsJsonObject());
                    String entryString = gson.toJson(elem);
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(entryString);
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                SRPMixins.LOGGER.error("SRPMixins: Failed to generate default loot table file, {}, {}", paraName, e);
            }
        }
    }

    public static JsonObject cleanUpJson(JsonObject elem){
        elem.remove("isFrozen"); //forge internal variable
        JsonArray pools = elem.getAsJsonArray("pools");
        for(JsonElement poolElem : pools){
            JsonObject poolObj = poolElem.getAsJsonObject();
            poolObj.remove("name"); //names are only going to confuse ppl
            for(JsonElement entryElem : poolObj.getAsJsonArray("entries")){
                JsonObject entryObj = entryElem.getAsJsonObject();
                entryObj.remove("entryName"); //names are only going to confuse ppl
                entryObj.remove("weight"); //uses the default value of 1
                entryObj.remove("quality"); //uses the default value of 0
            }
        }
        return elem;
    }

    public static LootPool getLootPool(LootEntry entry, String name){
        if(entry == null)
            return new LootPool(new LootEntry[0], noConditions, new RandomValueRange(1), new RandomValueRange(0), name);
        return new LootPool(new LootEntry[]{entry}, noConditions, new RandomValueRange(1), new RandomValueRange(0), name);
    }

    public static LootEntryItem getLootEntry(Item item, int maxCount, int chanceInHundred){
        return new LootEntryItem(
                item,
                1,
                0,
                new LootFunction[]{new SetCount(noConditions, new RandomValueRange(/*1*/maxCount, maxCount))},
                new LootCondition[]{new RandomChance(/*chanceInHundred*/100 / 100F)},
                "entry for 1 to " + maxCount + " of " + item.getRegistryName().toString() + ", chance " + chanceInHundred + "%"
        );
    }
}
