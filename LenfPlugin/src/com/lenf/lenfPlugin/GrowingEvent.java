package com.lenf.lenfPlugin;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import com.sun.scenario.effect.Crop;
import com.mojang.datafixers.util.Pair;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class GrowingEvent implements Listener {
    private final Plugin plugin;

    public GrowingEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Plant(BlockPlaceEvent e){
        if(LenfData.ItemToPlant.stream().anyMatch(x->x.getFirst().isSimilar(e.getItemInHand()))){
            if(e.getBlockPlaced().getLocation().subtract(0,1,0).getBlock().getType().equals(Material.FARMLAND)){
                Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);
                ArmorStand as = e.getPlayer().getWorld().spawn(new Location(e.getPlayer().getWorld(),0,0,0),ArmorStand.class);
                LenfData.CropArmor.put(location,as);
                as.setVisible(false);
                as.setGravity(false);
                as.setMarker(true);
                as.setBasePlate(false);
                as.setSilent(true);
                as.setCustomName("OzenForm");
                as.setCustomNameVisible(false);
                as.teleport(location);

                ItemStack item = LenfData.ItemToPlant.stream().filter(x->x.getFirst().isSimilar(e.getItemInHand())).findFirst().get().getSecond().getFirst();
                as.setHelmet(item);
            }
        }
    }

    @EventHandler
    public void Grow(BlockGrowEvent e){
        Location location = e.getBlock().getLocation().add(0.5,0,0.5);
        World world = e.getBlock().getWorld();
        if(LenfData.CropArmor.containsKey(location)){
            ArmorStand as = LenfData.CropArmor.get(location);

            e.setCancelled(true);

            ItemStack helmet = as.getEquipment().getHelmet();

            Pair<ItemStack, Pair<ItemStack, ItemStack>> Crops = LenfData.ItemToPlant.stream().filter(x->x.getSecond().getFirst().isSimilar(helmet)).findFirst().get();
            List<ItemStack> series = LenfData.ItemToPlant.stream().filter(x->x.getFirst().isSimilar(Crops.getFirst())).map(x->x.getSecond().getSecond()).collect(Collectors.toList());

            ItemStack Next = Crops.getSecond().getSecond();

            if(series.indexOf(Next) != series.size() - 1){
                as.getEquipment().setHelmet(Next);
            }
        }
    }

    @EventHandler
    public void ItemSpawnDestroy(ItemSpawnEvent e){
        if(!e.getEntity().getItemStack().isSimilar(new ItemStack(Material.WHEAT_SEEDS)) || !e.getEntity().getName().equals("Wheat Seeds"))
            return;
        Location location = e.getLocation();
        location.setX(Math.floor(location.getX()) + 0.5d);
        location.setY(Math.floor(location.getY()));
        location.setZ(Math.floor(location.getZ()) + 0.5d);
        location.setYaw(0);
        if(!LenfData.CropArmor.containsKey(location)){
            return;
        }
        e.setCancelled(true);
        World world = e.getEntity().getWorld();
        Destroy(location, world);
    }

    @EventHandler
    public void DestroyItem(BlockBreakEvent e){
        Location location = e.getBlock().getLocation().add(0.5,0,0.5);
        if(LenfData.CropArmor.get(location) == null){
            if(LenfData.CropArmor.get(location.add(0,1,0)) == null){
                return;
            }
        }
        e.setCancelled(true);
        e.getBlock().setType(Material.AIR);

        World world = e.getBlock().getWorld();
        Destroy(location, world);
    }

    void Destroy(Location location, World world){
        ArmorStand as = LenfData.CropArmor.get(location);
        LenfData.CropArmor.remove(location);

        ItemStack helmet = as.getEquipment().getHelmet();

        Pair<ItemStack, Pair<ItemStack, ItemStack>> Crops = LenfData.ItemToPlant.stream().filter(x->x.getSecond().getFirst().isSimilar(helmet)).findFirst().get();
        List<ItemStack> series = LenfData.ItemToPlant.stream().filter(x->x.getFirst().isSimilar(Crops.getFirst())).map(x->x.getSecond().getSecond()).collect(Collectors.toList());

        ItemStack Seed = Crops.getFirst();
        ItemStack Crop = Crops.getSecond().getSecond();

        if(series.indexOf(Crop) == series.size() - 1){
            Random r = new Random();
            for(int i = 0; i < r.nextInt(3) + 1; i++)
                world.dropItemNaturally(location,Seed);
            world.dropItemNaturally(location,Crop);
        }else{
            world.dropItemNaturally(location,Seed);
        }

        Bukkit.getServer().getScheduler().runTaskLater(plugin, as::remove, 1L);
    }


}
