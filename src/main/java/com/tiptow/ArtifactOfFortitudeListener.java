package com.tiptow;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
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

public class ArtifactOfFortitudeListener implements Listener {

    private final NamespacedKey fortitudeKey;
    private final NamespacedKey usedKey;
    private final JavaPlugin plugin;
    private final int maxUsage = 5;

    public ArtifactOfFortitudeListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.fortitudeKey = new NamespacedKey(plugin, "artifact_of_fortitude");
        this.usedKey = new NamespacedKey(plugin, "fortitude_usage");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return; // Check if the item has metadata
        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.getPersistentDataContainer().has(fortitudeKey, PersistentDataType.BYTE)) return;

        Player player = event.getPlayer();
        int usageCount = player.getPersistentDataContainer().getOrDefault(usedKey, PersistentDataType.INTEGER, 0);

        if (usageCount < maxUsage) {
            Attribute armorAttribute = Attribute.GENERIC_ARMOR;
            double currentArmor = player.getAttribute(armorAttribute).getBaseValue();
            player.getAttribute(armorAttribute).setBaseValue(currentArmor + 2.0); // Increase armor by 1, 2 points

            usageCount++;
            player.getPersistentDataContainer().set(usedKey, PersistentDataType.INTEGER, usageCount);
            player.sendMessage(ChatColor.GREEN + "You have used the Artifact of Fortitude! You gained +1 armor permanently.");
            player.sendMessage(ChatColor.YELLOW + "You have used the Artifact of Fortitude " + usageCount + " out of " + maxUsage + " times.");
            item.setAmount(item.getAmount() - 1);
        } else {
            player.sendMessage(ChatColor.RED + "You have already used the Artifact of Fortitude the maximum number of times (" + maxUsage + ").");
        }
    }

    public ItemStack createArtifactOfFortitude() {
        ItemStack artifact = new ItemStack(Material.ECHO_SHARD);
        ItemMeta meta = artifact.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Artifact of Fortitude");
            meta.getPersistentDataContainer().set(fortitudeKey, PersistentDataType.BYTE, (byte) 1);

            // Adding lore
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "Grants +1 Armor");
            lore.add(ChatColor.DARK_PURPLE + "--------------");
            lore.add("");
            lore.add(ChatColor.RED + "Max Uses: 5");
            meta.setLore(lore);

            artifact.setItemMeta(meta);
        }
        return artifact;
    }
}
