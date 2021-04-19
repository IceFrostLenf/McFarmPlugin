package com.lenf.lenfPlugin;

import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R1.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LenfItems {


//    public static final ArrayList<ItemStack> LenfItem = new ArrayList<>(Arrays.asList(
//            LenfItemStack(Material.STRING,"Test"),
//            LenfItemStack(Material.STRING,"Test1"),
//            LenfItemStack(Material.STRING,"Test2"),
//            LenfItemStack(Material.IRON_NUGGET,"brewingstand",null,"CustomModelData",2),
//
//            LenfItemStack(Material.WHEAT_SEEDS,"曼德拉草種子",null,"CustomModelData",1),
//            LenfItemStack(Material.IRON_NUGGET,"Seed3_0",null,"CustomModelData",3),
//            LenfItemStack(Material.IRON_NUGGET,"Seed3_1",null,"CustomModelData",4),
//            LenfItemStack(Material.IRON_NUGGET,"Seed3_2",null,"CustomModelData",5),
//            LenfItemStack(Material.IRON_NUGGET,"曼德拉的根",null,"CustomModelData",7),
//
//            LenfItemStack(Material.WHEAT_SEEDS,"TestSeed")
//    ));

    public static ItemStack GetLenfItem(String name){
        return LenfData.LenfItem.stream().filter(x->x.getItemMeta().getLocalizedName().equals(name)).findFirst().get();
    }

    public static class GetOzenItem implements CommandExecutor{
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(commandSender instanceof Player && strings.length >= 1 && strings.length <= 2){
                Player p = (Player)commandSender;
                if(!LenfData.PermissionList.contains(p.getName())){
                    return false;
                }
                if(strings.length == 1){
                    p.getInventory().addItem(GetLenfItem(strings[0]));
                }else{
                    int count = Integer.parseInt(strings[1]);
                    for(int i = 0; i < count; i++){
                        p.getInventory().addItem(GetLenfItem(strings[0]));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public static class TabOzenItem implements TabCompleter{
        @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
            if(commandSender instanceof Player){
                Player p = (Player)commandSender;
                if(!LenfData.PermissionList.contains(p.getName())){
                    return null;
                }
                if(strings.length == 1){
                    return LenfData.LenfItem.stream().map(x->x.getItemMeta().getLocalizedName()).collect(Collectors.toList());
                }
            }
            return null;
        }
    }

    public static org.bukkit.inventory.ItemStack LenfItemStack(Material material, String name){
        return LenfItemStack(material,name,null,null,0);
    }

//    public static org.bukkit.inventory.ItemStack LenfItemStack(Material material, String name, int customModelValue){
//        return LenfItemStack(material,name,null,null,0);
//    }

    public static org.bukkit.inventory.ItemStack LenfItemStack(Material material, String name, List<String> lore, String tag, int value){
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material);
//        if(tag != null){
//            net.minecraft.server.v1_16_R1.ItemStack nbtitem = CraftItemStack.asNMSCopy(item);
//            NBTTagCompound nbttag = new NBTTagCompound();
//            nbttag.set(tag,(NBTTagInt.a(value)));
//            nbtitem.setTag(nbttag);
//            item = CraftItemStack.asBukkitCopy(nbtitem);
//        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("\u00A7f" + name);
        itemMeta.setLocalizedName(name);
        if(lore != null)
            itemMeta.setLore(lore);
        if(tag != null)
            itemMeta.setCustomModelData(value);
        item.setItemMeta(itemMeta);
        return item;
    }
}
