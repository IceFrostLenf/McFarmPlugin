//package com.lenf.lenfPlugin;
//
//import com.sun.xml.internal.ws.api.ha.StickyFeature;
//import org.bukkit.Location;
//import org.bukkit.entity.ArmorStand;
//import org.bukkit.util.io.BukkitObjectInputStream;
//import org.bukkit.util.io.BukkitObjectOutputStream;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;
//
//public class LenfData implements Serializable {
//
//    static transient final String RootPath = "./plugins/Lenf";
//    static transient final String FileName = "/LenfData.data";
//
//    static transient LenfData instance = null;
//
//    int a = 77;
//
//    public final HashMap<Location, ArmorStand> PlaceArmor;
//    public final HashMap<Location, ArmorStand> CropArmor;
//
//    public static LenfData getInstance(){
//        return instance == null ? new LenfData() : instance;
//    }
//
//    public LenfData(LenfData loadedData) {
//        PlaceArmor = loadedData.PlaceArmor;
//        CropArmor = loadedData.CropArmor;
//    }
//    public LenfData(){
//        PlaceArmor = new HashMap<>();
//        CropArmor = new HashMap<>();
//    }
//
//    public static void enable(){
//        File file = new File(RootPath + FileName);
//        instance = file.exists() ? new LenfData(loadData(RootPath + FileName)) : new LenfData();
//    }
//    public static void disable(){
//        File file = new File(RootPath);
//        file.mkdir();
//        instance.saveData(RootPath + FileName);
//    }
//
//    public boolean saveData(String filePath) {
//        try {
//            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
//            out.writeObject(this);
//            out.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static LenfData loadData(String filePath) {
//        try {
//            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
//            LenfData data = (LenfData) in.readObject();
//            in.close();
//            return data;
//        } catch (ClassNotFoundException | IOException e) {
//            e.printStackTrace();
//            Main.plugin.getLogger().info(e.toString());
//            return null;
//        }
//    }
//
//}
package com.lenf.lenfPlugin;

import com.mojang.datafixers.util.Pair;
import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import sun.plugin2.main.server.Plugin;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LenfData implements Serializable {

    static final String RootPath = "./plugins/Lenf";
    static File ArmorFile = new File(RootPath + "/LenfArmor.yml");
    static File ItemFile = new File(RootPath + "/LenfItem.yml");
    static File BlockFile = new File(RootPath + "/LenfBlock.yml");
    static File PlantFile = new File(RootPath + "/LenfPlant.yml");
    static File PermissionFile = new File(RootPath + "/LenfPermission.yml");

    public static final ArrayList<ItemStack> LenfItem = new ArrayList<>();

    public static List<Pair<ItemStack,ItemStack>> ItemToBlock = new ArrayList<>();
    public static HashMap<Location, ArmorStand> PlaceArmor = new HashMap<>();

    public static List<Pair<ItemStack,Pair<ItemStack,ItemStack>>> ItemToPlant = new ArrayList<>();
    public static HashMap<Location, ArmorStand> CropArmor = new HashMap<>();

    public static List<String> PermissionList = new ArrayList<>();

    public static List<String> ErrorLog = new ArrayList<>();

    public static void Load(Logger logger){
        File file = new File(RootPath);
        file.mkdir();
        //<editor-fold desc="Armor Config">
        try {
            FileConfiguration ArmorDataConfig = new YamlConfiguration();
            ArmorDataConfig.load(ArmorFile);

            Set<Chunk> ChunkRecorder = new HashSet<>();
            List<Map<?, ?>> Armors = ArmorDataConfig.getMapList("Armors");

            for(Map.Entry<?, ?> armor : Armors.get(0).entrySet()){
                Location l = ((Location) armor.getKey());
                if(!ChunkRecorder.contains(l.getChunk())){
                    ChunkRecorder.add(l.getChunk());
                    Bukkit.getWorld("world").loadChunk(l.getChunk());
                }
                UUID uuid = UUID.fromString(armor.getValue().toString());
                ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(uuid);
                if(armorStand == null){
                    ErrorLog.add("The BlockArmorStand entity for which uuid is " + uuid.toString() + " is not found");
                    continue;
                }
                PlaceArmor.put(l, armorStand);
            }
            for(Map.Entry<?, ?> armor : Armors.get(1).entrySet()){
                Location l = ((Location) armor.getKey());
                if(!ChunkRecorder.contains(l.getChunk())){
                    ChunkRecorder.add(l.getChunk());
                    Bukkit.getWorld("world").loadChunk(l.getChunk());
                }
                UUID uuid = UUID.fromString(armor.getValue().toString());
                ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(uuid);
                if(armorStand == null){
                    ErrorLog.add("The BlockArmorStand entity for which uuid is " + uuid.toString() + " is not found");
                    continue;
                }
                CropArmor.put(l, armorStand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>
        //<editor-fold desc="Item Config">
        try{
            FileConfiguration ItemDataConfig = new YamlConfiguration();
            ItemDataConfig.load(ItemFile);

            LenfItem.clear();
            List<?> lenfItem = ItemDataConfig.getList("LenfItem", new ArrayList<>());

            lenfItem.forEach(x->{
                LenfItem.add(((ItemStack)x));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>
        //<editor-fold desc="Block Config">
        try{
            FileConfiguration BlockDataConfig = new YamlConfiguration();
            BlockDataConfig.load(BlockFile);

            List<?> lenfBlcok = BlockDataConfig.getList("LenfBlock", new ArrayList<>());
            ItemToBlock.clear();

            lenfBlcok.forEach(x->{
                List<String> data = ((List<String>) x);
                Pair<ItemStack, ItemStack> pair = new Pair<>(LenfItems.GetLenfItem(data.get(0)),LenfItems.GetLenfItem(data.get(1)));
                ItemToBlock.add(pair);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>
        //<editor-fold desc="Plant Config">
        try{
            FileConfiguration PlantDataConfig = new YamlConfiguration();
            PlantDataConfig.load(PlantFile);

            List<?> lenfPlant = PlantDataConfig.getList("LenfPlant",new ArrayList<>());
            ItemToPlant.clear();

            lenfPlant.forEach(x->{
                List<String> data = ((List<String>) x);
                Pair<ItemStack,ItemStack> pairSec = new Pair<>(LenfItems.GetLenfItem(data.get(1)),LenfItems.GetLenfItem(data.get(2)));
                Pair<ItemStack,Pair<ItemStack, ItemStack>> pair = new Pair<>(LenfItems.GetLenfItem(data.get(0)),pairSec);
                ItemToPlant.add(pair);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>
        //<editor-fold desc="Permission Config">
        try{
            FileConfiguration PermissionDataConfig = new YamlConfiguration();
            PermissionDataConfig.load(PermissionFile);

            PermissionList = PermissionDataConfig.getStringList("LenfPermission");

        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>
        if(ErrorLog.size() != 0){
            for (String s : ErrorLog) { logger.log(Level.WARNING, s); }
        }
    }

    public static void Save(){
        try {
            //<editor-fold desc="Armor Config">
            FileConfiguration ArmorDataConfig = new YamlConfiguration();

            HashMap<Location, String> placeArmor = new HashMap<>();
            HashMap<Location, String> cropArmor = new HashMap<>();

            for(Map.Entry<Location,ArmorStand> i : PlaceArmor.entrySet())
                placeArmor.put(i.getKey(), i.getValue().getUniqueId().toString());
            for(Map.Entry<Location,ArmorStand> i : CropArmor.entrySet())
                cropArmor.put(i.getKey(), i.getValue().getUniqueId().toString());

            ArmorDataConfig.set("Armors",new ArrayList<>(Arrays.asList(placeArmor, cropArmor)));
            ArmorDataConfig.save(ArmorFile);
            //</editor-fold

//            //<editor-fold desc="Item Config">
//            FileConfiguration ItemDataConfig = new YamlConfiguration();
//
//            ItemDataConfig.set("LenfItem",LenfItem);
//            ItemDataConfig.save(ItemFile);
//            //</editor-fold>
//
//            //<editor-fold desc="Block Config">
//            FileConfiguration BlockDataConfig = new YamlConfiguration();
//
//            List<List<String>> BlockData = new ArrayList<>();
//            for(Pair<ItemStack, ItemStack> p : ItemToBlock){
//                List<String> Data = new ArrayList<>();
//                Data.add((p.getFirst()).getItemMeta().getLocalizedName());
//                Data.add((p.getSecond()).getItemMeta().getLocalizedName());
//                BlockData.add(Data);
//            }
//
//            BlockDataConfig.set("LenfBlock",BlockData);
//            BlockDataConfig.save(BlockFile);
//            //</editor-fold>
//
//            //<editor-fold desc="Plant Config">
//            FileConfiguration PlantDataConfig = new YamlConfiguration();
//
//            List<List<String>> PlantData = new ArrayList<>();
//            for(Pair<ItemStack, Pair<ItemStack, ItemStack>> p : ItemToPlant){
//                List<String> Data = new ArrayList<>();
//                Data.add(p.getFirst().getItemMeta().getLocalizedName());
//                Data.add(p.getSecond().getFirst().getItemMeta().getLocalizedName());
//                Data.add(p.getSecond().getSecond().getItemMeta().getLocalizedName());
//                PlantData.add(Data);
//            }
//
//            PlantDataConfig.set("LenfPlant",PlantData);
//            PlantDataConfig.save(PlantFile);
//            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}