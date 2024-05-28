package com.tiptow;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ScepterListener implements Listener {

    private final NamespacedKey scepterKey;
    private final NamespacedKey usedKey;
    private final JavaPlugin plugin;

    public ScepterListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scepterKey = new NamespacedKey(plugin, "scepter_of_knowledge");
        this.usedKey = new NamespacedKey(plugin, "scepter_used");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.BOOK) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(scepterKey, PersistentDataType.BYTE)) return;

        if (!player.getPersistentDataContainer().has(usedKey, PersistentDataType.BYTE)) {
            // If the player hasn't used the scepter yet
            player.sendMessage(ChatColor.GREEN + "You have used the Artifact of Knowledge! Your experience gain is now increased by 10% permanently.");
            player.getPersistentDataContainer().set(usedKey, PersistentDataType.BYTE, (byte) 1);
        } else {
            // If the player has already used the scepter
            player.sendMessage(ChatColor.RED + "You have already used the Artifact of Knowledge.");
            return;
        }

        item.setAmount(item.getAmount() - 1);
    }

    public ItemStack createScepterOfKnowledge() {
        ItemStack scepter = new ItemStack(Material.BOOK);
        ItemMeta meta = scepter.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Artifact of Knowledge");
            meta.getPersistentDataContainer().set(scepterKey, PersistentDataType.BYTE, (byte) 1);

            // Adding lore
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "Grants XP Boosts");
            lore.add(ChatColor.DARK_PURPLE + "---------------");
            lore.add("");
            lore.add(ChatColor.RED + "Max Uses: 1");
            meta.setLore(lore);

            scepter.setItemMeta(meta);
        }
        return scepter;
    }
}
