package com.yotdev.ghostblock.item;

import com.yotdev.ghostblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class GhostBlockItem {

    private final Main plugin;
    public static ItemStack ghost_block = new ItemStack(Material.STONE, 1);
    public static ItemMeta ghost_block_meta = ghost_block.getItemMeta();

    public GhostBlockItem(Main plugin) {
        this.plugin = plugin;
    }


    public void createItem() {

        ghost_block_meta.setDisplayName("Ghost Block");
        ghost_block_meta.setLore(Collections.singletonList("Un block transperçable"));
        ghost_block.setItemMeta(ghost_block_meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "ghost_block"), ghost_block);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.REDSTONE);
        recipe.setIngredient('O', Material.STONE);

        Bukkit.addRecipe(recipe);
    }

}
