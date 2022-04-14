package me.supern64.placemc.commands;

import me.supern64.placemc.PlaceMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlaceCommand implements CommandExecutor {
    private final PlaceMC plugin;

    public PlaceCommand(PlaceMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getHelpMessage());
        } else {
            if (!args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("blocks")) {
                if (!sender.hasPermission("placemc.manage")) {
                    sender.sendMessage(ChatColor.RED + "You can't use this command!");
                    return true;
                }
            }
            switch (args[0]) {
                case "reload":
                    plugin.reloadConfig();
                    plugin.loadConfigIntoMemory();
                    sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                    break;
                case "toggle":
                    plugin.setActivated(!plugin.isActivated());
                    plugin.resaveConfig();
                    sender.sendMessage(plugin.isActivated() ? ChatColor.GREEN + "Place mode activated!" : ChatColor.RED + "Place mode deactivated!");
                    break;
                case "setcooldown":
                    if (args.length >= 2) {
                        plugin.setCooldown(Integer.parseInt(args[1]));
                        plugin.resaveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Set cooldown to " + args[1] + " milliseconds!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Not enough arguments: /place setcooldown [cooldown]");
                    }
                    break;
                case "sety":
                    if (args.length >= 3) {
                        plugin.setMinY(Integer.parseInt(args[1]));
                        plugin.setMaxY(Integer.parseInt(args[2]));
                        plugin.resaveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Set Y level range from " + args[1] + " to " + args[2] + "!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Not enough arguments: /place sety [min] [max]");
                    }
                    break;
                case "worlds":
                    if (args.length >= 2) {
                        switch (args[1]) {
                            case "add":
                                if (args.length >= 3) {
                                    if (plugin.getActivatedWorlds().contains(args[2])) {
                                        sender.sendMessage(ChatColor.RED + "World " + args[2] + " is already activated!");
                                    } else {
                                        plugin.getActivatedWorlds().add(args[2]);
                                        plugin.resaveConfig();
                                        sender.sendMessage(ChatColor.GREEN + "Added world " + args[2] + " to activated worlds!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Not enough arguments: /place worlds add [world]");
                                }
                                break;
                            case "remove":
                                if (args.length >= 3) {
                                    if (!plugin.getActivatedWorlds().contains(args[2])) {
                                        sender.sendMessage(ChatColor.RED + "World " + args[2] + " is not activated!");
                                    } else {
                                        plugin.getActivatedWorlds().remove(args[2]);
                                        plugin.resaveConfig();
                                        sender.sendMessage(ChatColor.GREEN + "Removed world " + args[2] + " from activated worlds!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Not enough arguments: /place worlds remove [world]");
                                }
                                break;
                            case "list":
                                if (plugin.getActivatedWorlds().size() == 0) {
                                    sender.sendMessage(ChatColor.RED + "No worlds are activated!");
                                } else {
                                    sender.sendMessage(ChatColor.GREEN + "Activated worlds: " + String.join(", ", plugin.getActivatedWorlds()));
                                }
                                break;
                            default:
                                sender.sendMessage(ChatColor.RED + "Invalid command: /place worlds [add/remove/list]");
                        }
                    }
                    break;
                case "blocks":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "You must be a player to use this command!"); 
                    } else {
                        Player player = (Player) sender;
			if (!plugin.isActivated() || !plugin.getActivatedWorlds().contains(player.getWorld().getName())) {
                            sender.sendMessage(ChatColor.RED + "Place mode is not enabled here!");
			} else {
                            Inventory inventory = Bukkit.createInventory(null, 18, "Blocks!");
                            for (int i = 0; i < 16; i++) {
                                inventory.addItem(new ItemStack(Material.WOOL, 64, (short) i));
                            }
                            player.openInventory(inventory);
			}
                    }
                    break;
                default:
                    sender.sendMessage(getHelpMessage());
            }
        }
        return true;
    }

    private String getHelpMessage() {
        return ChatColor.DARK_AQUA + "PlaceMC 1.0.0 - by SuperN64\n" +
                "/place help - Shows this message\n" +
                "/place reload - Reloads the config\n" +
                "/place toggle - Toggles whether place mode will be activated\n" +
                "/place worlds [add/remove/list] - Change the list of activated worlds\n" +
                "/place sety [min] [max] - Set the minimum and maximum Y values blocks can be placed or broken at\n" +
                "/place setcooldown [cooldown] - Set the cooldown in milliseconds\n" +
                "/place blocks - Get some blocks for placing";
    }
}
