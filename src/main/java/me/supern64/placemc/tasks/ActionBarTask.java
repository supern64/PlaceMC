package me.supern64.placemc.tasks;

import me.supern64.placemc.PlaceMC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class ActionBarTask extends BukkitRunnable {
    private PlaceMC plugin;

    public ActionBarTask(PlaceMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.isActivated()) {
            Map<UUID, Long> placedBlock = plugin.getPlacedBlock();
            for (Map.Entry<UUID, Long> entry : placedBlock.entrySet()) {
                Player player = plugin.getServer().getPlayer(entry.getKey());
		if (player == null) {
		    continue;
		}
                if (plugin.getActivatedWorlds().contains(player.getWorld().getName())) {
                    long remaining = plugin.getCooldown() - (System.currentTimeMillis() - entry.getValue());
                    if (remaining > 0) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.RED + "You can place or destroy another block in " + ChatColor.YELLOW + String.format("%.2f", remaining / 1000.0) + ChatColor.RED + " seconds.")
                        );
                    } else {
                        plugin.getPlacedBlock().remove(entry.getKey());
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "You can now place or destroy another block!"));
                    }
                }
            }
        }
    }
}
