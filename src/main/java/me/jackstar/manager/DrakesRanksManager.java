package me.jackstar.drakesranks.manager;

import me.jackstar.drakesranks.domain.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DrakesRanksManager {

    private final JavaPlugin plugin;
    private final Map<String, Rank> ranks = new HashMap<>();
    private final Map<UUID, String> playerRanks = new HashMap<>();
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();
    private final File file;

    public DrakesRanksManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "ranks.yml");
        saveDefaultFile();
        reload();
    }

    public void reload() {
        ranks.clear();
        playerRanks.clear();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection ranksSection = config.getConfigurationSection("ranks");
        if (ranksSection != null) {
            for (String rankName : ranksSection.getKeys(false)) {
                String path = "ranks." + rankName;
                String prefix = config.getString(path + ".prefix", "<gray>[Member]</gray>");
                String suffix = config.getString(path + ".suffix", "");
                String color = config.getString(path + ".color", "<gray>");
                int weight = config.getInt(path + ".weight", 0);
                List<String> nodes = config.getStringList(path + ".permissions");
                ranks.put(normalize(rankName), new Rank(rankName, prefix, suffix, color, weight, nodes));
            }
        }

        ConfigurationSection playersSection = config.getConfigurationSection("players");
        if (playersSection != null) {
            for (String uuid : playersSection.getKeys(false)) {
                try {
                    playerRanks.put(UUID.fromString(uuid), normalize(playersSection.getString(uuid)));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        for (Rank rank : ranks.values()) {
            String path = "ranks." + rank.getName();
            config.set(path + ".prefix", rank.getPrefix());
            config.set(path + ".suffix", rank.getSuffix());
            config.set(path + ".color", rank.getColor());
            config.set(path + ".weight", rank.getWeight());
            config.set(path + ".permissions", new ArrayList<>(rank.getPermissionNodes()));
        }
        for (Map.Entry<UUID, String> entry : playerRanks.entrySet()) {
            config.set("players." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException ex) {
            plugin.getLogger().warning("Could not save ranks.yml: " + ex.getMessage());
        }
    }

    public Optional<Rank> findRank(String rankName) {
        return Optional.ofNullable(ranks.get(normalize(rankName)));
    }

    public List<Rank> getRanks() {
        return new ArrayList<>(ranks.values());
    }

    public boolean createRank(String rankName) {
        String key = normalize(rankName);
        if (key == null || ranks.containsKey(key)) {
            return false;
        }
        ranks.put(key, new Rank(rankName, "<gray>[Member]</gray>", "", "<gray>", 0, List.of()));
        save();
        return true;
    }

    public boolean addPermissionToRank(String rankName, String node) {
        Rank rank = ranks.get(normalize(rankName));
        if (rank == null || node == null || node.isBlank()) {
            return false;
        }
        if (!rank.getPermissionNodes().contains(node)) {
            rank.getPermissionNodes().add(node);
            save();
        }
        return true;
    }

    public boolean setPlayerRank(UUID uuid, String rankName) {
        if (uuid == null || !ranks.containsKey(normalize(rankName))) {
            return false;
        }
        playerRanks.put(uuid, normalize(rankName));
        save();
        return true;
    }

    public Rank getPlayerRank(Player player) {
        String rankKey = playerRanks.getOrDefault(player.getUniqueId(), "member");
        return ranks.getOrDefault(rankKey, ranks.values().stream().findFirst()
                .orElse(new Rank("Member", "<gray>[Member]</gray>", "", "<gray>", 0, List.of())));
    }

    public void applyPermissions(Player player) {
        clearAttachment(player);
        Rank rank = getPlayerRank(player);
        PermissionAttachment attachment = player.addAttachment(plugin);
        for (String node : rank.getPermissionNodes()) {
            attachment.setPermission(node, true);
        }
        attachments.put(player.getUniqueId(), attachment);
    }

    public void clearAttachment(Player player) {
        PermissionAttachment old = attachments.remove(player.getUniqueId());
        if (old != null) {
            player.removeAttachment(old);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        return normalized.isEmpty() ? null : normalized;
    }

    private void saveDefaultFile() {
        if (plugin.getResource("ranks.yml") != null) {
            plugin.saveResource("ranks.yml", false);
            return;
        }
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }
}
