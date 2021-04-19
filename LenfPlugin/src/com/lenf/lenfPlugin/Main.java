package com.lenf.lenfPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements CommandExecutor, TabCompleter {
    static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        LenfData.Load(getLogger());
        getCommand("lenf").setExecutor(this);
        getCommand("lenf").setTabCompleter(this);

        getCommand("ozenitem").setExecutor(new LenfItems.GetOzenItem());
        getCommand("ozenitem").setTabCompleter(new LenfItems.TabOzenItem());
        // ForFindEvent
//        getCommand("behaviorDetect").setExecutor(new BehaviorDetector.DetecterSetting());
//        getCommand("behaviorDetect").setTabCompleter(new BehaviorDetector.TabDetecterSetting());
        //
        getServer().getPluginManager().registerEvents(new ItemsEvent(this),this);
        getServer().getPluginManager().registerEvents(new GrowingEvent(this),this);
        getServer().getPluginManager().registerEvents(new BehaviorDetector(), this);
    }

    @Override
    public void onDisable() {
        LenfData.Save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(s.equals("lenf") && args.length == 1){
            if(args[0].equals("save")){
                if(sender instanceof Player){
                    Player p = (Player) sender;
                    LenfData.Save();
                    p.sendMessage("Save Success!");
                    return true;
                }
            }else if(args[0].equals("reload")){
                if(sender instanceof Player){
                    Player p = (Player) sender;
                    LenfData.Save();
                    LenfData.Load(getLogger());
                    p.sendMessage("Reload Success!");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(sender instanceof Player){
            if(args.length == 1){
                ArrayList<String> tab = new ArrayList<>(Arrays.asList("save", "reload"));
                return tab.stream().filter(x->x.contains(args[0])).collect(Collectors.toList());
            }
        }
        return null;
    }
}


