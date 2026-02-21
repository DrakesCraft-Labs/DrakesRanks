package me.jackstar.drakesranks;

import me.jackstar.drakesranks.commands.RankCommand;
import me.jackstar.drakesranks.integration.papi.DrakesRanksPlaceholderExpansion;
import me.jackstar.drakesranks.listeners.DrakesRanksListener;
import me.jackstar.drakesranks.manager.DrakesRanksManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DrakesRanksPlugin extends JavaPlugin {

    private static final String[] DRAGON_BANNER = {
            "              / \\  //\\",
            "      |\\___/|      /   \\//  \\\\",
            "      /O  O  \\__  /    //  | \\ \\",
            "     /     /  \\/_/    //   |  \\  \\",
            "     \\_^_\\'/   \\/_   //    |   \\   \\"
    };

    private DrakesRanksManager ranksManager;

    @Override
    public void onEnable() {
        logDragonBanner("DrakesRanks");
        logLoading("Loading ranks manager");
        ranksManager = new DrakesRanksManager(this);
        logLoading("Registering listeners");
        getServer().getPluginManager().registerEvents(new DrakesRanksListener(ranksManager), this);

        logLoading("Registering command executors");
        PluginCommand rankCommand = getCommand("rank");
        if (rankCommand != null) {
            rankCommand.setExecutor(new RankCommand(ranksManager));
        } else {
            getLogger().warning("Command 'rank' not found in plugin.yml.");
        }

        logLoading("Registering PlaceholderAPI expansion if available");
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new DrakesRanksPlaceholderExpansion(ranksManager).register();
        }

        getLogger().info("[Ready] DrakesRanks enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[Shutdown] DrakesRanks stopping...");
        if (ranksManager != null) {
            ranksManager.save();
        }
        getLogger().info("[Shutdown] DrakesRanks disabled.");
    }

    private void logLoading(String step) {
        getLogger().info("[Loading] " + step + "...");
    }

    private void logDragonBanner(String pluginName) {
        getLogger().info("========================================");
        getLogger().info(" " + pluginName + " - loading");
        for (String line : DRAGON_BANNER) {
            getLogger().info(line);
        }
        getLogger().info("========================================");
    }
}
