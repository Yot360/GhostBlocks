package com.yotdev.ghostblock.events;

import com.yotdev.ghostblock.Main;
import com.yotdev.ghostblock.item.GhostBlockItem;
import com.yotdev.ghostblock.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakListener implements Listener {


    private Main plugin;

    public BreakListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata("ghost")) {
            Block block = event.getBlock();
            block.setType(Material.AIR);
            for (Player p : Bukkit.getOnlinePlayers()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    block.setType(Material.AIR);
                }, 20);
            }


            // Remove block and update near blocks
            PlaceListener.map.remove(LocationSerializer.sLoc(event.getBlock().getLocation()));
            for (BlockFace blockface : BlockFace.values()) {
                if (PlaceListener.map.containsKey(LocationSerializer.sLoc(event.getBlock().getLocation())) && blockface != BlockFace.SELF && event.getBlock().getType() != Material.AIR) {
                    Block nxtBlock = event.getBlock().getRelative(blockface);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            p.sendBlockChange(nxtBlock.getLocation(), Bukkit.createBlockData(PlaceListener.map.get(LocationSerializer.sLoc(nxtBlock.getLocation()))));
                        }, 1);
                    }
                }
            }

            // Set custom drop
            event.setDropItems(false);
            block.getDrops(GhostBlockItem.ghost_block).clear();
            block.getWorld().dropItemNaturally(block.getLocation(), GhostBlockItem.ghost_block);
            block.setType(Material.AIR);

        }
    }

}
