package com.yotdev.ghostblock.utils;

import com.google.gson.Gson;
import com.yotdev.ghostblock.Main;
import com.yotdev.ghostblock.events.PlaceListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSONHelper {

    public static Map<String, String> loadJson(File file) {

        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(file.toPath());

            // convert JSON file to map
            Map<String, String> map = gson.fromJson(reader, Map.class);

            // close reader & return map
            reader.close();
            return map;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public static void saveToJson(File file) {
        try{
            // create Gson instance
            Gson gson = new Gson();

            // convert map object to JSON file
            String json = gson.toJson(PlaceListener.map);

            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createJson(Main plugin, File file) {
        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }
    }

}
