package srpmixins.config.providers;

import com.dhanantry.scapeandrunparasites.util.SRPReference;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.google.common.io.Files;
import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;

import javax.annotation.Nullable;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class LootPoolProvider {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
            .registerTypeAdapter(LootPool.class, new LootPool.Serializer())
            .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
            .registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer())
            .registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer())
            .registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer())
            .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
            .setPrettyPrinting().create();

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

    private static void registerLootTable(String paraName, LootTable table){
        ResourceLocation lootTableLoc = new ResourceLocation(SRPReference.MOD_ID, "entities/"+paraName);
        int paraId = SRPMobConfigProvider.mobNameToParaIdMap.getOrDefault(paraName, Integer.MIN_VALUE);
        if(paraId != Integer.MIN_VALUE) {
            lootTableLocations.put(paraId, lootTableLoc);
            lootTables.put(lootTableLoc, table);
            LootTableList.register(lootTableLoc); //register the pool in vanilla logic
        } else {
            SRPMixins.LOGGER.warn("SRPMixins unable to register parasite loot table, no parasite id registered for {}", paraName);
        }
    }

    public static void getLootPoolsFromConfigOrFile() {
        List<String> readParaNames = readLootPoolsFromFile();
        getLootPoolsFromConfigAndWriteToFile(readParaNames);
    }

    public static final ThreadLocal<Boolean> readingFromSRPMixinsConfig = ThreadLocal.withInitial(() -> false);
    private static List<String> readLootPoolsFromFile(){
        List<String> readNames = new ArrayList<>();

        File[] files = lootTablesFolder.listFiles();
        if(files == null) {
            SRPMixins.LOGGER.error("Failed to load loot table, folder is invalid");
            SRPMixins.LOGGER.info("=== Finishing SRPMixins loot table Data Loading ===");
            return readNames;
        }
        for(File file : files) {
            if(file.isDirectory()) continue;
            JsonElement elem = getJson(file);
            if(elem == null) {
                SRPMixins.LOGGER.warn("Failed to parse loot table file: {}", file.getName());
                continue;
            }
            try {
                readingFromSRPMixinsConfig.set(true); //needed bc forge is a bish
                LootTable lootTable = gson.fromJson(elem, LootTable.class);
                readingFromSRPMixinsConfig.set(false);

                if(lootTable == null) SRPMixins.LOGGER.warn("Failed to load loot table file, invalid file: {}", file.getName());
                else {
                    String paraName = file.getName().split("\\.json")[0];
                    registerLootTable(paraName, lootTable);
                    readNames.add(paraName);
                }
            }
            catch(Exception e) {
                SRPMixins.LOGGER.warn("Failed to load loot table file: {}, {}", file.getName(), e);
                e.printStackTrace(System.out);
            }
        }

        return readNames;
    }

    @Nullable
    @SuppressWarnings("UnstableApiUsage")
    private static JsonElement getJson(File file) {
        if(file == null || !file.exists()) {
            SRPMixins.LOGGER.warn("Failed to load SRPMixins config file, file does not exist");
            return null;
        }

        try {
            if(!file.setReadable(true)) {
                SRPMixins.LOGGER.warn("Failed to load SRPMixins config file, no permission to read the file: {}", file.getName());
                return null;
            }
            String fileString = Files.toString(file, Charset.defaultCharset());
            return new JsonParser().parse(fileString);
        }
        catch(Exception e) {
            SRPMixins.LOGGER.warn("Failed to load SRPMixins config file: {}, {}", file.getName(), e);
        }
        return null;
    }

    private static void getLootPoolsFromConfigAndWriteToFile(List<String> readParaNames){
        Map<String, String[]> lootTableConfigs = new HashMap<String, String[]>(){{
            put("pri_longarms", SRPConfigMobs.shycoLoot);
            put("sim_bigspider", SRPConfigMobs.dorpaLoot);
            put("carrier_heavy", SRPConfigMobs.ratholLoot);
            put("pri_yelloweye", SRPConfigMobs.emanaLoot);
            put("buglin", SRPConfigMobs.LodoLoot);
            put("sim_human", SRPConfigMobs.infhumanLoot);
            put("pri_manducater", SRPConfigMobs.hullLoot);
            put("pri_summoner", SRPConfigMobs.canraLoot);
            put("overseer", SRPConfigMobs.alafhaLoot);
            put("pri_reeker", SRPConfigMobs.noglaLoot);
            put("carrier_flying", SRPConfigMobs.butholLoot);
            put("rupter", SRPConfigMobs.mudoLoot);
            put("sim_cow", SRPConfigMobs.infcowLoot);
            put("sim_sheep", SRPConfigMobs.infsheepLoot);
            put("sim_wolf", SRPConfigMobs.infwolfLoot);
            put("beckon_si", SRPConfigMobs.venkrolLoot);
            put("pri_bolster", SRPConfigMobs.zetmoLoot);
            put("beckon_sii", SRPConfigMobs.venkrolsiiLoot);
            put("beckon_siii", SRPConfigMobs.venkrolsiiiLoot);
            put("anc_overlord", SRPConfigMobs.terlaLoot);
            put("sim_wolfhead", SRPConfigMobs.infwolfheadLoot);
            put("sim_sheephead", SRPConfigMobs.infsheepheadLoot);
            put("movingflesh", SRPConfigMobs.kolLoot);
            put("anc_dreadnaut", SRPConfigMobs.oroncoLoot);
            put("vigilante", SRPConfigMobs.angedLoot);
            put("sim_pig", SRPConfigMobs.infpigLoot);
            put("sim_villager", SRPConfigMobs.infvillagerLoot);
            put("sim_cowhead", SRPConfigMobs.infcowheadLoot);
            put("kyphosis", SRPConfigMobs.tonroLoot);
            put("sentry", SRPConfigMobs.unvoLoot);
            put("sim_pighead", SRPConfigMobs.infpigheadLoot);
            put("sim_villagerhead", SRPConfigMobs.infvillagerheadLoot);
            put("warden", SRPConfigMobs.ganroLoot);
            put("anc_pod", SRPConfigMobs.pod1Loot);
            put("worker", SRPConfigMobs.kolLoot);
            put("pri_tozoon", SRPConfigMobs.wymoLoot);
            put("pri_arachnida", SRPConfigMobs.arachnidaLoot);
            put("incompleteform_small", SRPConfigMobs.inhooSLoot);
            put("sim_adventurer", SRPConfigMobs.infadventurerLoot);
            put("beckon_siv", SRPConfigMobs.venkrolsivLoot);
            put("incompleteform_medium", SRPConfigMobs.inhooMLoot);
            put("sim_horse", SRPConfigMobs.infhorseLoot);
            put("sim_horsehead", SRPConfigMobs.infhorseheadLoot);
            put("sim_humanhead", SRPConfigMobs.infhumanheadLoot);
            put("bomber_light", SRPConfigMobs.ombooLoot);
            put("host", SRPConfigMobs.hostLoot);
            put("sim_bear", SRPConfigMobs.infbearLoot);
            put("marauder", SRPConfigMobs.esorLoot);
            put("ada_longarms", SRPConfigMobs.shycoadaptedloot);
            put("ada_manducater", SRPConfigMobs.hulladaptedloot);
            put("ada_summoner", SRPConfigMobs.canraadaptedloot);
            put("ada_reeker", SRPConfigMobs.noglaadaptedloot);
            put("ada_yelloweye", SRPConfigMobs.emanaadaptedloot);
            put("ada_bolster", SRPConfigMobs.zetmoadaptedloot);
//put("??", SRPConfigMobs.shycoLoot); //idk what mob thats supposed to be
            put("ada_arachnida", SRPConfigMobs.arachnidaadaptedloot);
            put("sim_enderman", SRPConfigMobs.infendermanLoot);
            put("grunt", SRPConfigMobs.flogLoot);
            put("crux", SRPConfigMobs.cruxaLoot);
            put("heed", SRPConfigMobs.heedLoot);
            put("sim_dragone", SRPConfigMobs.infdragoneLoot);
            put("bomber_heavy", SRPConfigMobs.jinjoLoot);
            put("pri_devourer", SRPConfigMobs.lumLoot);
            put("sim_endermanhead", SRPConfigMobs.infendermanheadLoot);
            put("sim_dragonehead", SRPConfigMobs.infdragoneheadLoot);
            put("sim_adventurerhead", SRPConfigMobs.infadventurerheadLoot);
            put("seizer", SRPConfigMobs.nakLoot);
            put("dispatcher_si", SRPConfigMobs.dodsiLoot);
            put("dispatcherten", SRPConfigMobs.ratholLoot);
            put("hostii", SRPConfigMobs.herdLoot);
            put("mangler", SRPConfigMobs.nuuhLoot);
            put("dispatcher_sii", SRPConfigMobs.dodsiiLoot);
            put("dispatcher_siii", SRPConfigMobs.dodsiiiLoot);
            put("dispatcher_siv", SRPConfigMobs.dodsivLoot);
            put("thrall", SRPConfigMobs.thrallLoot);
//put("lum_adapted", SRPConfigMobs.lumadaptedloot); //mob doesnt exist yet
            put("seeker", SRPConfigMobs.ombooLoot);
            put("monarch", SRPConfigMobs.orchLoot);
            put("wraith", SRPConfigMobs.elviaLoot);
            put("bogle", SRPConfigMobs.lenciaLoot);
            put("haunter", SRPConfigMobs.pheonLoot);
            put("carrier_colony", SRPConfigMobs.vestaLoot);
            put("succor", SRPConfigMobs.flamLoot);
            put("gnat", SRPConfigMobs.ataLoot);
            put("pri_vermin", SRPConfigMobs.ikiLoot);
            put("fer_cow", SRPConfigMobs.fercowLoot);
            put("fer_enderman", SRPConfigMobs.ferendermanLoot);
            put("fer_horse", SRPConfigMobs.ferhorseLoot);
            put("fer_human", SRPConfigMobs.ferhumanLoot);
            put("fer_pig", SRPConfigMobs.ferpigLoot);
            put("fer_sheep", SRPConfigMobs.fersheepLoot);
            put("fer_villager", SRPConfigMobs.fervillagerLoot);
            put("fer_wolf", SRPConfigMobs.ferwolfLoot);
            put("hi_golem", SRPConfigMobs.higolemLoot);
            put("carrier_light", SRPConfigMobs.gotholLoot);
            put("fer_bear", SRPConfigMobs.ferbearLoot);
            put("sim_squid", SRPConfigMobs.infsquidLoot);
            put("draconite", SRPConfigMobs.hebluLoot);
        }};

        for (Map.Entry<String, String[]> entry : lootTableConfigs.entrySet()) {
            if(readParaNames.contains(entry.getKey())) continue; //Skipping loot table that already exist in config/srpmixins/loot_tables/

            LootTable mobLoot = new LootTable(new LootPool[0]);
            LootPool pool_dropOnlyOne = createLootPool(null, "drop only one of these");
            int dropOnlyOneCounter = 0;
            String lastDropOnlyOneNormalName = null;
            LootEntryItem lastDropOnlyOneEntry = null;

            String paraName = entry.getKey();
            if(paraName == null){
                SRPMixins.LOGGER.error("SRPMixins unable to find parasite {}", entry.getKey());
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

                        String normalName = "pool for " + item.getRegistryName().toString();
                        if (independentDrop)
                            mobLoot.addPool(createLootPool(createLootEntry(item, maxQuantity, chance), normalName));
                        else{
                            lastDropOnlyOneEntry = createLootEntry(item, maxQuantity, chance);
                            dropOnlyOneCounter++;
                            lastDropOnlyOneNormalName = normalName;
                            pool_dropOnlyOne.addEntry(lastDropOnlyOneEntry);
                        }
                    } catch (Exception e) {
                        SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line for {}, expected String; int; int; boolean: {}", paraName, s);
                    }
                }
            }

            if(dropOnlyOneCounter > 1)
                mobLoot.addPool(pool_dropOnlyOne);
            else if(dropOnlyOneCounter == 1)
                mobLoot.addPool(createLootPool(lastDropOnlyOneEntry, lastDropOnlyOneNormalName)); //register them as normal loot pools if they only contain one item entry

            registerLootTable(paraName, mobLoot);

            //Write to config/srpmixins/loot_tables/para_name.json
            try {
                File file = new File(lootTablesFolder, String.format("%s.json", paraName));
                if(file.exists()) continue; //don't write if the file already exists, shouldn't happen
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

    private static JsonObject cleanUpJson(JsonObject elem){
        elem.remove("isFrozen"); //forge internal variable
        JsonArray pools = elem.getAsJsonArray("pools");
        for(JsonElement poolElem : pools){
            JsonObject poolObj = poolElem.getAsJsonObject();
            //poolObj.remove("name"); //names are only going to confuse ppl
            for(JsonElement entryElem : poolObj.getAsJsonArray("entries")){
                JsonObject entryObj = entryElem.getAsJsonObject();
                entryObj.remove("entryName"); //names are only going to confuse ppl
                entryObj.remove("weight"); //uses the default value of 1
                entryObj.remove("quality"); //uses the default value of 0
            }
        }
        return elem;
    }

    private static final LootCondition[] noConditions = new LootCondition[0];

    private static LootPool createLootPool(LootEntry entry, String name){
        if(entry == null)
            return new LootPool(new LootEntry[0], noConditions, new RandomValueRange(1), new RandomValueRange(0), name);
        return new LootPool(new LootEntry[]{entry}, noConditions, new RandomValueRange(1), new RandomValueRange(0), name);
    }

    private static LootEntryItem createLootEntry(Item item, int maxCount, int chanceInHundred) {
        return new LootEntryItem(
                item,
                1,
                0,
                new LootFunction[]{
                        new SetCount(noConditions, new RandomValueRange(1, maxCount)),
                        new LootingEnchantBonus(noConditions, new RandomValueRange(0), 0)
                },
                new LootCondition[]{new RandomChanceWithLooting(chanceInHundred / 100F, 0)},
                "entry for 1 to " + maxCount + " of " + item.getRegistryName().toString() + ", chance " + chanceInHundred + "%"
        );
    }
}
