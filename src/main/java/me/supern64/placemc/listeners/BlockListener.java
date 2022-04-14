package me.supern64.placemc.listeners;

import me.supern64.placemc.PlaceMC;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    private final PlaceMC plugin;

    public BlockListener(PlaceMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.isActivated() && !event.isCancelled() && plugin.getActivatedWorlds().contains(event.getBlock().getWorld().getName())) {
            if (event.getBlock().getY() < plugin.getMinY() || event.getBlock().getY() > plugin.getMaxY()) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can't place blocks here!");
                event.setCancelled(true);
                return;
            }

            if (plugin.getPlacedBlock().containsKey(event.getPlayer().getUniqueId()) && System.currentTimeMillis() - plugin.getPlacedBlock().get(event.getPlayer().getUniqueId()) < plugin.getCooldown()) {
                long remaining = plugin.getCooldown() - (System.currentTimeMillis() - plugin.getPlacedBlock().get(event.getPlayer().getUniqueId()));
                event.getPlayer().sendMessage(
                    ChatColor.RED + "You must wait " + ChatColor.YELLOW + String.format("%.2f", remaining / 1000.0) + ChatColor.RED + " seconds before placing another block."
                );
                event.setCancelled(true);
            } else {
                plugin.getPlacedBlock().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.isActivated() && !event.isCancelled() && plugin.getActivatedWorlds().contains(event.getBlock().getWorld().getName())) {
            if (event.getBlock().getY() < plugin.getMinY() || event.getBlock().getY() > plugin.getMaxY()) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can't break blocks here!");
                event.setCancelled(true);
                return;
            }

            if (plugin.getPlacedBlock().containsKey(event.getPlayer().getUniqueId()) && System.currentTimeMillis() - plugin.getPlacedBlock().get(event.getPlayer().getUniqueId()) < plugin.getCooldown()) {
                long remaining = plugin.getCooldown() - (System.currentTimeMillis() - plugin.getPlacedBlock().get(event.getPlayer().getUniqueId()));
                event.getPlayer().sendMessage(
                    ChatColor.RED + "You must wait " + ChatColor.YELLOW + String.format("%.2f", remaining / 1000.0) + ChatColor.RED + " seconds before breaking another block."
                );
                event.setCancelled(true);
            } else {
                plugin.getPlacedBlock().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }
}
