package com.yotdev.ghostblock;

import com.yotdev.ghostblock.events.*;
import com.yotdev.ghostblock.item.GhostBlockItem;
import com.yotdev.ghostblock.utils.JSONHelper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    Logger logger = this.getLogger();
    public File blocksSave = new File(getDataFolder(), "blocks.json");

    @Override
    public void onEnable() {
        logger.log(Level.INFO, ChatColor.GREEN + "GhostBlock plugin started");

        // Check if json exist: if it does -> load it, if not -> create it
        JSONHelper.createJson(this, blocksSave);
        if (JSONHelper.loadJson(blocksSave) != null)
            PlaceListener.map = JSONHelper.loadJson(blocksSave);

        new GhostBlockItem(this).createItem();
        getServer().getPluginManager().registerEvents(new PlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new BreakListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, ChatColor.RED + "GhostBlock plugin stopped");
        JSONHelper.saveToJson(blocksSave);
    }

    public File getSaveFile() {
        return blocksSave;
    }
}
