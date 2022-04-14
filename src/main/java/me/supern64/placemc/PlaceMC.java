package me.supern64.placemc;

import me.supern64.placemc.commands.PlaceCommand;
import me.supern64.placemc.listeners.BlockListener;
import me.supern64.placemc.tasks.ActionBarTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlaceMC extends JavaPlugin {
    private final Map<UUID, Long> placedBlock = new HashMap<>(); // time the person place the block
    private long cooldown;
    private int minY;
    private int maxY;
    private boolean isActivated;
    private List<String> activatedWorlds;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getCommand("place").setExecutor(new PlaceCommand(this));
        loadConfigIntoMemory();
        new ActionBarTask(this).runTaskTimer(this, 0, 20);
    }

    public void loadConfigIntoMemory() {
        this.saveDefaultConfig();
        cooldown = this.getConfig().getLong("cooldown");
        minY = this.getConfig().getInt("level.min");
        maxY = this.getConfig().getInt("level.max");
        isActivated = this.getConfig().getBoolean("activated");
        activatedWorlds = this.getConfig().getStringList("activatedWorlds");
    }

    public void resaveConfig() {
        this.getConfig().set("cooldown", cooldown);
        this.getConfig().set("level.min", minY);
        this.getConfig().set("level.max", maxY);
        this.getConfig().set("activated", isActivated);
        this.getConfig().set("activatedWorlds", activatedWorlds);
        this.saveConfig();
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public List<String> getActivatedWorlds() {
        return activatedWorlds;
    }

    public Map<UUID, Long> getPlacedBlock() {
        return placedBlock;
    }
}
