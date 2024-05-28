package com.tiptow;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("ArtifactPlugin has been enabled!");

        // Register event listeners
        getServer().getPluginManager().registerEvents(new ScepterListener(this), this);
        getServer().getPluginManager().registerEvents(new ArtifactOfVitalityListener(this), this);
        getServer().getPluginManager().registerEvents(new ArtifactOfFortitudeListener(this), this);
        getServer().getPluginManager().registerEvents(new ArtifactOfAgilityListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ArtifactPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("giveartifact")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
                return true;
            }

            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("vitality")) {
                    player.getInventory().addItem(new ArtifactOfVitalityListener(this).createArtifactOfVitality());
                    player.sendMessage(ChatColor.GREEN + "You have received the Artifact of Vitality!");
                } else if (args[0].equalsIgnoreCase("scepter")) {
                    player.getInventory().addItem(new ScepterListener(this).createScepterOfKnowledge());
                    player.sendMessage(ChatColor.GREEN + "You have received the Scepter of Knowledge!");
                } else if (args[0].equalsIgnoreCase("fortitude")) {
                    player.getInventory().addItem(new ArtifactOfFortitudeListener(this).createArtifactOfFortitude());
                    player.sendMessage(ChatColor.GREEN + "You have received the Artifact of Fortitude!");
                } else if (args[0].equalsIgnoreCase("agility")) {
                    player.getInventory().addItem(new ArtifactOfAgilityListener(this).createArtifactOfAgility());
                    player.sendMessage(ChatColor.GREEN + "You have received the Artifact of Agility!");
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid artifact name. Please specify vitality, scepter, fortitude, or agility.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Please specify which artifact you would like to receive.");
                player.sendMessage(ChatColor.GREEN + "Options are: vitality, scepter, fortitude, or agility");
            }
            return true;
        }
        return false;
    }

    private void clearPersistentData(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer() != null) {
                    for (NamespacedKey key : meta.getPersistentDataContainer().getKeys()) {
                        meta.getPersistentDataContainer().remove(key);
                    }
                    item.setItemMeta(meta);
                }
            }
        }
    }
}
