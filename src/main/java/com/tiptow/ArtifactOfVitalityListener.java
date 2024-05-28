package com.tiptow;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

public class ArtifactOfVitalityListener implements Listener {

    private final NamespacedKey vitalityKey;
    private final NamespacedKey usedKey;
    private final JavaPlugin plugin;
    private final int maxUsage = 10;

    public ArtifactOfVitalityListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.vitalityKey = new NamespacedKey(plugin, "artifact_of_vitality");
        this.usedKey = new NamespacedKey(plugin, "vitality_usage");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.AMETHYST_SHARD) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(vitalityKey, PersistentDataType.BYTE)) return;

        Player player = event.getPlayer();
        int usageCount = player.getPersistentDataContainer().getOrDefault(usedKey, PersistentDataType.INTEGER, 0);

        if (usageCount < maxUsage) {
            usageCount++;
            player.getPersistentDataContainer().set(usedKey, PersistentDataType.INTEGER, usageCount);

            AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (healthAttribute != null) {
                healthAttribute.setBaseValue(healthAttribute.getBaseValue() + 2.0);
            }
            player.sendMessage(ChatColor.GREEN + "You have used the Artifact of Vitality! Your maximum health is now increased by 1 heart permanently.");
            player.sendMessage(ChatColor.YELLOW + "You have used the Artifact of Vitality " + usageCount + " out of " + maxUsage + " times.");
            item.setAmount(item.getAmount() - 1);
        } else {
            player.sendMessage(ChatColor.RED + "You have already used the Artifact of Vitality the maximum number of times (" + maxUsage + ").");
        }

    }

    public ItemStack createArtifactOfVitality() {
        ItemStack artifact = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = artifact.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Artifact of Vitality");
            meta.getPersistentDataContainer().set(vitalityKey, PersistentDataType.BYTE, (byte) 1);

            // Adding lore
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "Grants An");
            lore.add(ChatColor.GOLD + "Additional Heart");
            lore.add(ChatColor.DARK_PURPLE + "--------------");
            lore.add("");
            lore.add(ChatColor.RED + "Max Uses: 10");
            meta.setLore(lore);

            artifact.setItemMeta(meta);
        }
        return artifact;
    }
}
