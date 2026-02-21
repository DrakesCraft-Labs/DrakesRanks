package me.jackstar.drakesranks.commands;

import me.jackstar.drakescraft.utils.MessageUtils;
import me.jackstar.drakesranks.domain.Rank;
import me.jackstar.drakesranks.manager.DrakesRanksManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RankCommand implements CommandExecutor {

    private final DrakesRanksManager ranksManager;

    public RankCommand(DrakesRanksManager ranksManager) {
        this.ranksManager = ranksManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!sender.hasPermission("drakesranks.admin")) {
            MessageUtils.send(sender, "<red>No permission.</red>");
            return true;
        }

        if (args.length == 0) {
            usage(sender, label);
            return true;
        }

        if ("set".equalsIgnoreCase(args[0])) {
            return handleSet(sender, label, args);
        }
        if ("create".equalsIgnoreCase(args[0])) {
            return handleCreate(sender, label, args);
        }
        if ("permission".equalsIgnoreCase(args[0])) {
            return handlePermission(sender, label, args);
        }
        if ("list".equalsIgnoreCase(args[0])) {
            return handleList(sender);
        }
        if ("info".equalsIgnoreCase(args[0])) {
            return handleInfo(sender, label, args);
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            ranksManager.reload();
            for (Player player : Bukkit.getOnlinePlayers()) {
                ranksManager.applyPermissions(player);
            }
            MessageUtils.send(sender, "<green>DrakesRanks reloaded.</green>");
            return true;
        }

        usage(sender, label);
        return true;
    }

    private boolean handleSet(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            MessageUtils.send(sender, "<red>Usage: /" + label + " set <player> <rank></red>");
            return true;
        }

        UUID targetUuid = resolveTargetUuid(args[1]);
        if (targetUuid == null) {
            MessageUtils.send(sender, "<red>Player not found. Join once or use exact name in offline mode.</red>");
            return true;
        }
        if (!ranksManager.setPlayerRank(targetUuid, args[2])) {
            MessageUtils.send(sender, "<red>Unknown rank.</red>");
            return true;
        }

        Player online = Bukkit.getPlayer(targetUuid);
        if (online != null && online.isOnline()) {
            ranksManager.applyPermissions(online);
            MessageUtils.send(online, "<green>Your rank is now <yellow>" + args[2] + "</yellow>.</green>");
        }

        MessageUtils.send(sender, "<green>Rank updated for <yellow>" + args[1] + "</yellow>.</green>");
        return true;
    }

    private boolean handleCreate(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            MessageUtils.send(sender, "<red>Usage: /" + label + " create <name></red>");
            return true;
        }
        if (!ranksManager.createRank(args[1])) {
            MessageUtils.send(sender, "<red>Rank already exists or invalid name.</red>");
            return true;
        }
        MessageUtils.send(sender, "<green>Rank created: <yellow>" + args[1] + "</yellow>.</green>");
        return true;
    }

    private boolean handlePermission(CommandSender sender, String label, String[] args) {
        if (args.length < 4 || !"add".equalsIgnoreCase(args[1])) {
            MessageUtils.send(sender, "<red>Usage: /" + label + " permission add <rank> <node></red>");
            return true;
        }
        if (!ranksManager.addPermissionToRank(args[2], args[3])) {
            MessageUtils.send(sender, "<red>Could not add permission. Check rank/node.</red>");
            return true;
        }
        MessageUtils.send(sender, "<green>Permission added.</green>");
        return true;
    }

    private boolean handleList(CommandSender sender) {
        MessageUtils.send(sender, "<yellow>Ranks by weight:</yellow>");
        for (Rank rank : ranksManager.getRanksSortedByWeight()) {
            MessageUtils.send(sender, "<gray>-</gray> <yellow>" + rank.getName() + "</yellow> <gray>(weight "
                    + rank.getWeight() + ", nodes " + rank.getPermissionNodes().size() + ")</gray>");
        }
        MessageUtils.send(sender, "<gray>Default rank key:</gray> <aqua>" + ranksManager.getDefaultRankKey() + "</aqua>");
        return true;
    }

    private boolean handleInfo(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            MessageUtils.send(sender, "<red>Usage: /" + label + " info <rank></red>");
            return true;
        }

        Rank rank = ranksManager.findRank(args[1]).orElse(null);
        if (rank == null) {
            MessageUtils.send(sender, "<red>Unknown rank.</red>");
            return true;
        }

        MessageUtils.send(sender, "<yellow>Rank info:</yellow> <aqua>" + rank.getName() + "</aqua>");
        MessageUtils.send(sender, "<gray>Weight:</gray> <aqua>" + rank.getWeight() + "</aqua>");
        MessageUtils.send(sender, "<gray>Prefix:</gray> " + rank.getPrefix());
        MessageUtils.send(sender, "<gray>Suffix:</gray> " + rank.getSuffix());
        MessageUtils.send(sender, "<gray>Color:</gray> " + rank.getColor());
        MessageUtils.send(sender, "<gray>Permissions:</gray> <aqua>" + String.join(", ", rank.getPermissionNodes()) + "</aqua>");
        return true;
    }

    private void usage(CommandSender sender, String label) {
        MessageUtils.send(sender, "<yellow>/"+label+" set <player> <rank></yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" create <name></yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" permission add <rank> <node></yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" list</yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" info <rank></yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" reload</yellow>");
    }

    private UUID resolveTargetUuid(String playerName) {
        Player online = Bukkit.getPlayerExact(playerName);
        if (online != null) {
            return online.getUniqueId();
        }

        OfflinePlayer cached = Bukkit.getOfflinePlayerIfCached(playerName);
        if (cached != null) {
            return cached.getUniqueId();
        }

        if (!Bukkit.getOnlineMode()) {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
        }

        return null;
    }
}
