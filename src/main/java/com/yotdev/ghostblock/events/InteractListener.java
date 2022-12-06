package com.yotdev.ghostblock.events;

import com.yotdev.ghostblock.Main;
import com.yotdev.ghostblock.item.GhostBlockItem;
import com.yotdev.ghostblock.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.Map;

public class InteractListener implements Listener {

    private final Main plugin;

    public InteractListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTouch(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && (!event.getPlayer().getInventory().getItemInMainHand().getType().isBlock() || event.getPlayer().getInventory().getItemInMainHand().getType().isAir())) {
            if (event.getClickedBlock().hasMetadata("ghost") && PlaceListener.map.containsKey(LocationSerializer.sLoc(event.getClickedBlock().getLocation()))) {
                Player player = event.getPlayer();
                Block block = event.getClickedBlock();
                if (block.getType() != Material.AIR) {
                    block.setType(Material.AIR);
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        block.setType(Bukkit.createBlockData(PlaceListener.map.get(LocationSerializer.sLoc(block.getLocation()))).getMaterial());
                        p.sendBlockChange(block.getLocation(), Bukkit.createBlockData(PlaceListener.map.get(LocationSerializer.sLoc(block.getLocation()))));
                    }, 3 * 20);
                }
            }
        }

        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getPlayer().getInventory().getItemInMainHand().getType().isBlock() && event.getClickedBlock().hasMetadata("ghost")) {
            Block block = event.getClickedBlock();
            for (Player p : Bukkit.getOnlinePlayers()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    p.sendBlockChange(block.getLocation(), Bukkit.createBlockData(PlaceListener.map.get(LocationSerializer.sLoc(block.getLocation()))));
                }, 1);
            }
        }

    }
}
