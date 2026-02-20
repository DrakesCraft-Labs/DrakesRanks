package me.jackstar.drakesranks;

import me.jackstar.drakesranks.commands.RankCommand;
import me.jackstar.drakesranks.integration.papi.DrakesRanksPlaceholderExpansion;
import me.jackstar.drakesranks.listeners.DrakesRanksListener;
import me.jackstar.drakesranks.manager.DrakesRanksManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DrakesRanksPlugin extends JavaPlugin {

    private DrakesRanksManager ranksManager;

    @Override
    public void onEnable() {
        ranksManager = new DrakesRanksManager(this);
        getServer().getPluginManager().registerEvents(new DrakesRanksListener(ranksManager), this);

        PluginCommand rankCommand = getCommand("rank");
        if (rankCommand != null) {
            rankCommand.setExecutor(new RankCommand(ranksManager));
        } else {
            getLogger().warning("Command 'rank' not found in plugin.yml.");
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new DrakesRanksPlaceholderExpansion(ranksManager).register();
        }
    }

    @Override
    public void onDisable() {
        if (ranksManager != null) {
            ranksManager.save();
        }
    }
}
