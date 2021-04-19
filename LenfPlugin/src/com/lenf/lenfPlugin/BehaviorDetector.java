package com.lenf.lenfPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

public class BehaviorDetector implements Listener {
    public int[] eventTime = new int[34];
    public static Map<String, Boolean> eventTrigger = new HashMap<String, Boolean>() {
        {
            put("BlockCookEvent", false);
            put("BlockBurnEvent", false);
            put("BlockCanBuildEvent", false);
            put("BlockDamageEvent", false);
            put("BlockDispenseEvent", false);
            put("BlockDispenseArmorEvent", false);
            put("BlockDropItemEvent", false);
            put("BlockFadeEvent", false);
            put("BlockFertilizeEvent", false);
            put("BlockFormEvent", false);
            put("BlockFromToEvent", false);
            put("BlockGrowEvent", false);
            put("BlockIgniteEvent", false);
            put("BlockPhysicsEvent", false);
            put("BlockPistonExtendEvent", false);
            put("BlockPistonRetractEvent", false);
            put("BlockPlaceEvent", false);
            put("BlockRedstoneEvent", false);
            put("BlockSpreadEvent", false);
            put("BlockShearEntityEvent", false);
            put("BlockExpEvent", false);
            put("BlockExplodeEvent", false);
            put("BlockMultiPlaceEvent", false);
            put("EntityBlockFormEvent", false);
            put("CauldronLevelChangeEvent", false);
            put("FluidLevelChangeEvent", false);
            put("MoistureChangeEvent", false);
            put("LeavesDecayEvent", false);
            put("NotePlayEvent", false);
            put("SignChangeEvent", false);
            put("SpongeAbsorbEvent", false);
            put("BlockBreakEvent", false);
            put("EntityChangeBlockEvent",false);
            put("ItemSpawnEvent",false);
        }
    };
    public static class DetecterSetting implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(commandSender instanceof Player && strings.length == 2){
                eventTrigger.put(strings[0],strings[1].equals("true"));
                return true;
            }
            return false;
        }
    }

    public static class TabDetecterSetting implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
            if(commandSender instanceof Player){
                if(strings.length == 1){
                    return eventTrigger.keySet().stream().map(String::toLowerCase).filter(x -> x.contains(strings[0])).collect(Collectors.toList());
                }else if(strings.length == 2){
                    return new ArrayList<String>(){{add("true");add("false");}};
                }
            }
            return null;
        }
    }


    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if (eventTrigger.get("BlockBreakEvent")) {
            Bukkit.broadcastMessage("BlockBreakEvent " + (eventTime[0]++));
        }
    }

    @EventHandler
    public void BlockCookEvent(BlockCookEvent e) {
        if (eventTrigger.get("BlockCookEvent")) {
            Bukkit.broadcastMessage("BlockCookEvent " + (eventTime[1]++));
        }
    }

    @EventHandler
    public void BlockBurnEvent(BlockBurnEvent e) {
        if (eventTrigger.get("BlockBurnEvent")) {
            Bukkit.broadcastMessage("BlockBurnEvent " + (eventTime[2]++));
        }
    }

    @EventHandler
    public void BlockCanBuildEvent(BlockCanBuildEvent e) {
        if (eventTrigger.get("BlockCanBuildEvent")) {
            Bukkit.broadcastMessage("BlockCanBuildEvent " + (eventTime[3]++));
        }
    }

    @EventHandler
    public void BlockDamageEvent(BlockDamageEvent e) {
        if (eventTrigger.get("BlockDamageEvent")) {
            Bukkit.broadcastMessage("BlockDamageEvent " + (eventTime[4]++));
        }
    }

    @EventHandler
    public void BlockDispenseEvent(BlockDispenseEvent e) {
        if (eventTrigger.get("BlockDispenseEvent")) {
            Bukkit.broadcastMessage("BlockDispenseEvent " + (eventTime[5]++));
        }
    }

    @EventHandler
    public void BlockDispenseArmorEvent(BlockDispenseArmorEvent e) {
        if (eventTrigger.get("BlockDispenseArmorEvent")) {
            Bukkit.broadcastMessage("BlockDispenseArmorEvent " + (eventTime[6]++));
        }
    }

    @EventHandler
    public void BlockDropItemEvent(BlockDropItemEvent e) {
        if (eventTrigger.get("BlockDropItemEvent")) {
            Bukkit.broadcastMessage("BlockDropItemEvent " + (eventTime[7]++));
        }
    }

    @EventHandler
    public void BlockFadeEvent(BlockFadeEvent e) {
        if (eventTrigger.get("BlockFadeEvent")) {
            Bukkit.broadcastMessage("BlockFadeEvent " + (eventTime[8]++));
        }
    }

    @EventHandler
    public void BlockFertilizeEvent(BlockFertilizeEvent e) {
        if (eventTrigger.get("BlockFertilizeEvent")) {
            Bukkit.broadcastMessage("BlockFertilizeEvent " + (eventTime[9]++));
        }
    }

    @EventHandler
    public void BlockFormEvent(BlockFormEvent e) {
        if (eventTrigger.get("BlockFormEvent")) {
            Bukkit.broadcastMessage("BlockFormEvent " + (eventTime[10]++));
        }
    }

    @EventHandler
    public void BlockFromToEvent(BlockFromToEvent e) {
        if (eventTrigger.get("BlockFromToEvent")) {
            Bukkit.broadcastMessage("BlockFromToEvent " + (eventTime[11]++));
        }
    }

    @EventHandler
    public void BlockGrowEvent(BlockGrowEvent e) {
        if (eventTrigger.get("BlockGrowEvent")) {
            Bukkit.broadcastMessage("BlockGrowEvent " + (eventTime[12]++));
        }
    }

    @EventHandler
    public void BlockIgniteEvent(BlockIgniteEvent e) {
        if (eventTrigger.get("BlockIgniteEvent")) {
            Bukkit.broadcastMessage("BlockIgniteEvent " + (eventTime[13]++));
        }
    }

    @EventHandler
    public void BlockPhysicsEvent(BlockPhysicsEvent e) {
        if (eventTrigger.get("BlockPhysicsEvent")) {
            Bukkit.broadcastMessage("BlockPhysicsEvent " + (eventTime[14]++));
        }
    }

    @EventHandler
    public void BlockPistonExtendEvent(BlockPistonExtendEvent e) {
        if (eventTrigger.get("BlockPistonExtendEvent")) {
            Bukkit.broadcastMessage("BlockPistonExtendEvent " + (eventTime[15]++));
        }
    }

    @EventHandler
    public void BlockPistonRetractEvent(BlockPistonRetractEvent e) {
        if (eventTrigger.get("BlockPistonRetractEvent")) {
            Bukkit.broadcastMessage("BlockPistonRetractEvent " + (eventTime[16]++));
        }
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (eventTrigger.get("BlockPlaceEvent")) {
            Bukkit.broadcastMessage("BlockPlaceEvent " + (eventTime[17]++));
        }
    }

    @EventHandler
    public void BlockRedstoneEvent(BlockRedstoneEvent e) {
        if (eventTrigger.get("BlockRedstoneEvent")) {
            Bukkit.broadcastMessage("BlockRedstoneEvent " + (eventTime[18]++));
        }
    }

    @EventHandler
    public void BlockSpreadEvent(BlockSpreadEvent e) {
        if (eventTrigger.get("BlockSpreadEvent")) {
            Bukkit.broadcastMessage("BlockSpreadEvent " + (eventTime[19]++));
        }
    }

    @EventHandler
    public void BlockShearEntityEvent(BlockShearEntityEvent e) {
        if (eventTrigger.get("BlockShearEntityEvent")) {
            Bukkit.broadcastMessage("BlockShearEntityEvent " + (eventTime[20]++));
        }
    }

    @EventHandler
    public void BlockExpEvent(BlockExpEvent e) {
        if (eventTrigger.get("BlockExpEvent")) {
            Bukkit.broadcastMessage("BlockExpEvent " + (eventTime[21]++));
        }
    }

    @EventHandler
    public void BlockExplodeEvent(BlockExplodeEvent e) {
        if (eventTrigger.get("BlockExplodeEvent")) {
            Bukkit.broadcastMessage("BlockExplodeEvent " + (eventTime[22]++));
        }
    }

    @EventHandler
    public void BlockMultiPlaceEvent(BlockMultiPlaceEvent e) {
        if (eventTrigger.get("BlockMultiPlaceEvent")) {
            Bukkit.broadcastMessage("BlockMultiPlaceEvent " + (eventTime[23]++));
        }
    }

    @EventHandler
    public void EntityBlockFormEvent(EntityBlockFormEvent e) {
        if (eventTrigger.get("EntityBlockFormEvent")) {
            Bukkit.broadcastMessage("EntityBlockFormEvent " + (eventTime[24]++));
        }
    }

    @EventHandler
    public void CauldronLevelChangeEvent(CauldronLevelChangeEvent e) {
        if (eventTrigger.get("CauldronLevelChangeEvent")) {
            Bukkit.broadcastMessage("CauldronLevelChangeEvent " + (eventTime[25]++));
        }
    }

    @EventHandler
    public void FluidLevelChangeEvent(FluidLevelChangeEvent e) {
        if (eventTrigger.get("FluidLevelChangeEvent")) {
            Bukkit.broadcastMessage("FluidLevelChangeEvent " + (eventTime[26]++));
        }
    }

    @EventHandler
    public void MoistureChangeEvent(MoistureChangeEvent e) {
        if (eventTrigger.get("MoistureChangeEvent")) {
            Bukkit.broadcastMessage("MoistureChangeEvent " + (eventTime[27]++));
        }
    }

    @EventHandler
    public void LeavesDecayEvent(LeavesDecayEvent e) {
        if (eventTrigger.get("LeavesDecayEvent")) {
            Bukkit.broadcastMessage("LeavesDecayEvent " + (eventTime[28]++));
        }
    }

    @EventHandler
    public void NotePlayEvent(NotePlayEvent e) {
        if (eventTrigger.get("NotePlayEvent")) {
            Bukkit.broadcastMessage("NotePlayEvent " + (eventTime[29]++));
        }
    }

    @EventHandler
    public void SignChangeEvent(SignChangeEvent e) {
        if (eventTrigger.get("SignChangeEvent")) {
            Bukkit.broadcastMessage("SignChangeEvent " + (eventTime[30]++));
        }
    }

    @EventHandler
    public void SpongeAbsorbEvent(SpongeAbsorbEvent e) {
        if (eventTrigger.get("SpongeAbsorbEvent")) {
            Bukkit.broadcastMessage("SpongeAbsorbEvent " + (eventTime[31]++));
        }
    }

    @EventHandler
    public void EntityChangeBlockEvent(EntityChangeBlockEvent e){
        if (eventTrigger.get("EntityChangeBlockEvent")) {
            Bukkit.broadcastMessage("EntityChangeBlockEvent " + (eventTime[32]++));
        }
    }
    @EventHandler
    public void ItemSpawnEvent(ItemSpawnEvent e){
        if (eventTrigger.get("ItemSpawnEvent")) {
            Bukkit.broadcastMessage("------------------ItemSpawnEvent------------------");
            Bukkit.broadcastMessage("ItemSpawnEvent " + (eventTime[33]++));
            Bukkit.broadcastMessage(e.getLocation().toString());
            Bukkit.broadcastMessage(e.getEntity().toString());
            Bukkit.broadcastMessage(e.getEntity().getItemStack().toString());
            Bukkit.broadcastMessage("------------------ItemSpawnEvent------------------");
        }
    }
}
