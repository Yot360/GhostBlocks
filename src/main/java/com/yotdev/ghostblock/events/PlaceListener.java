package com.yotdev.ghostblock.events;

import com.yotdev.ghostblock.Main;
import com.yotdev.ghostblock.item.GhostBlockItem;
import com.yotdev.ghostblock.utils.JSONHelper;
import com.yotdev.ghostblock.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceListener implements Listener {

    private final Main plugin;

    public static Map<String, String> map = new HashMap<>();

    public PlaceListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack placed = new ItemStack(event.getItemInHand());

        if (placed.isSimilar(GhostBlockItem.ghost_block)) {
            Block block = event.getBlock();
            Block copy_block = event.getBlockAgainst();
            Player player = event.getPlayer();

            if (event.getBlockReplacedState().getType().isOccluding() || event.getBlockReplacedState().getType().isAir())
                createGhostBlock(player, block, copy_block, plugin);
            else {

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

                block.setType(Material.AIR);



                event.setCancelled(true);
            }
        }
        // if item placed isn't ghost block
        else {
            Block block = event.getBlock();

            for (BlockFace blockface : BlockFace.values()) {
                if (PlaceListener.map.containsKey(LocationSerializer.sLoc(block.getRelative(blockface).getLocation())) && blockface != BlockFace.SELF) {
                    Block nxtBlock = event.getBlock().getRelative(blockface);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            p.sendBlockChange(nxtBlock.getLocation(), Bukkit.createBlockData(PlaceListener.map.get(LocationSerializer.sLoc(nxtBlock.getLocation()))));
                        }, 1);
                    }
                }
            }
        }

    }

    public static void createGhostBlock(Player player, Block block, Block copy_block, Main plugin) {
        block.setMetadata("ghost", new FixedMetadataValue(plugin, true));

        //check if block is ghost block
        if (copy_block.hasMetadata("ghost")) {
            if (copy_block.getType() != Material.AIR) {
                String copyloc = LocationSerializer.sLoc(copy_block.getLocation());
                String loc = LocationSerializer.sLoc(block.getLocation());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> p.sendBlockChange(block.getLocation(), Bukkit.createBlockData(map.get(copyloc))), 2); //needs mini 2ts delay
                    Bukkit.getScheduler().runTaskLater(plugin, () -> p.sendBlockChange(copy_block.getLocation(), Bukkit.createBlockData(map.get(copyloc))), 2); //needs mini 2ts delay
                }
                map.put(loc, map.get(copyloc));
            }
        }
        else {
            if (copy_block.getType() != Material.AIR) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.sendBlockChange(block.getLocation(), copy_block.getBlockData());
                }, 2); //needs mini 2ts delay
                map.put(LocationSerializer.sLoc(block.getLocation()), copy_block.getBlockData().getAsString());
            }
        }

        //JSONHelper.saveToJson(plugin.getSaveFile());
    }

}
