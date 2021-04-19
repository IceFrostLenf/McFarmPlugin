package com.lenf.lenfPlugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.sun.scenario.effect.Crop;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemsEvent implements Listener {
    private final Plugin plugin;
    public ItemsEvent(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void PlaceItem(BlockPlaceEvent e){
        if(LenfData.ItemToBlock.stream().anyMatch(x -> x.getFirst().isSimilar(e.getItemInHand()))){
            Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);

            ArmorStand as = e.getPlayer().getWorld().spawn(new Location(e.getPlayer().getWorld(),0,0,0),ArmorStand.class);
            LenfData.PlaceArmor.put(location,as);
            as.setVisible(false);
            as.setGravity(false);
            as.setMarker(true);
            as.setBasePlate(false);
            as.setSilent(true);
            as.setCustomName("OzenForm");
            as.setCustomNameVisible(false);
            as.teleport(location);

            ItemStack item = LenfData.ItemToBlock.stream().filter(x->x.getFirst().isSimilar(e.getItemInHand())).findFirst().get().getSecond();
            as.setHelmet(item);
        }
    }

    @EventHandler
    public void DestroyItem(BlockBreakEvent e){
        Location location = e.getBlock().getLocation().add(0.5,0,0.5);
        World world = e.getBlock().getWorld();
        if(LenfData.PlaceArmor.containsKey(location)){
            ArmorStand as = LenfData.PlaceArmor.get(location);
            LenfData.PlaceArmor.remove(location);

            e.setCancelled(true);

            ItemStack helmet = as.getEquipment().getHelmet();
            e.getBlock().setType(Material.AIR);
            world.dropItemNaturally(location,LenfData.ItemToBlock.stream().filter(x->x.getSecond().isSimilar(helmet)).findFirst().get().getFirst());
            Bukkit.getServer().getScheduler().runTaskLater(plugin, as::remove, 1L);
        }
    }

    void Destroy(Location location, World world){
        if(LenfData.PlaceArmor.containsKey(location)){
            ArmorStand as = LenfData.PlaceArmor.get(location);
            LenfData.PlaceArmor.remove(location);

            ItemStack helmet = as.getEquipment().getHelmet();
            location.subtract(0.5,0,0.5).getBlock().getDrops().clear();
            world.dropItemNaturally(location,LenfData.ItemToBlock.stream().filter(x->x.getSecond().isSimilar(helmet)).findFirst().get().getFirst());
            Bukkit.getServer().getScheduler().runTaskLater(plugin, as::remove, 1L);
        }
    }

    @EventHandler
    public void FluidBreak(BlockFromToEvent e){
        Location location = e.getToBlock().getLocation().add(0.5,0,0.5);
        World world = e.getBlock().getWorld();
        Destroy(location, world);
    }

    @EventHandler
    public void PistonBreak(BlockPistonExtendEvent e){
        for(Block block : e.getBlocks()){
            Location location = block.getLocation().add(0.5,0,0.5);
            World world = e.getBlock().getWorld();
            Destroy(location, world);
        }
    }


}
