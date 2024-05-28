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

public class ArtifactOfAgilityListener implements Listener {

    private final NamespacedKey agilityKey;
    private final NamespacedKey usedKey;
    private final JavaPlugin plugin;
    private final int maxUsage = 2;

    public ArtifactOfAgilityListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.agilityKey = new NamespacedKey(plugin, "artifact_of_agility");
        this.usedKey = new NamespacedKey(plugin, "agility_usage");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.FEATHER) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(agilityKey, PersistentDataType.BYTE)) return;

        Player player = event.getPlayer();
        int usageCount = player.getPersistentDataContainer().getOrDefault(usedKey, PersistentDataType.INTEGER, 0);

        if (usageCount < maxUsage) {
            Attribute agilityAttribute = Attribute.GENERIC_MOVEMENT_SPEED;
            double currentSpeed = player.getAttribute(agilityAttribute).getBaseValue();
            player.getAttribute(agilityAttribute).setBaseValue(currentSpeed + 0.05); // Increase movement speed by 5%

            usageCount++;
            player.getPersistentDataContainer().set(usedKey, PersistentDataType.INTEGER, usageCount);
            player.sendMessage(ChatColor.GREEN + "You have used the Artifact of Agility! Your movement speed has increased by 5%.");
            player.sendMessage(ChatColor.YELLOW + "You have used the Artifact of Agility " + usageCount + " out of " + maxUsage + " times.");
            item.setAmount(item.getAmount() - 1);
        } else {
            player.sendMessage(ChatColor.RED + "You have already used the Artifact of Agility the maximum number of times (" + maxUsage + ").");
        }

    }

    public ItemStack createArtifactOfAgility() {
        ItemStack artifact = new ItemStack(Material.FEATHER);
        ItemMeta meta = artifact.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Artifact of Agility");
            meta.getPersistentDataContainer().set(agilityKey, PersistentDataType.BYTE, (byte) 1);

            // Adding lore
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "Grants 5% More");
            lore.add(ChatColor.GOLD + "Movement Speed");
            lore.add(ChatColor.DARK_PURPLE + "--------------");
            lore.add("");
            lore.add(ChatColor.RED + "Max Uses: 2");
            meta.setLore(lore);

            artifact.setItemMeta(meta);
        }
        return artifact;
    }
}
