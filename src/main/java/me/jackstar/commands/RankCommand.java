package me.jackstar.drakesranks.commands;

import me.jackstar.drakescraft.utils.MessageUtils;
import me.jackstar.drakesranks.manager.DrakesRanksManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

        usage(sender, label);
        return true;
    }

    private boolean handleSet(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            MessageUtils.send(sender, "<red>Usage: /" + label + " set <player> <rank></red>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            MessageUtils.send(sender, "<red>Player not found.</red>");
            return true;
        }
        if (!ranksManager.setPlayerRank(target.getUniqueId(), args[2])) {
            MessageUtils.send(sender, "<red>Unknown rank.</red>");
            return true;
        }

        ranksManager.applyPermissions(target);
        MessageUtils.send(sender, "<green>Rank updated.</green>");
        MessageUtils.send(target, "<green>Your rank is now <yellow>" + args[2] + "</yellow>.</green>");
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

    private void usage(CommandSender sender, String label) {
        MessageUtils.send(sender, "<yellow>/"+label+" set <player> <rank></yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" create <name></yellow>");
        MessageUtils.send(sender, "<yellow>/"+label+" permission add <rank> <node></yellow>");
    }
}
