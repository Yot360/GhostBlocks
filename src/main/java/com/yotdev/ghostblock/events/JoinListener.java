package com.yotdev.ghostblock.events;

import com.yotdev.ghostblock.Main;
import com.yotdev.ghostblock.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Map.Entry<String, String> block : PlaceListener.map.entrySet()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    player.sendBlockChange(LocationSerializer.dLoc(block.getKey()), Bukkit.createBlockData(block.getValue()));
                }
            }, 5);
        }
    }
}
