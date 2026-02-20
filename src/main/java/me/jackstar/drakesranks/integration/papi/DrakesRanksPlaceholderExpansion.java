package me.jackstar.drakesranks.integration.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jackstar.drakesranks.domain.Rank;
import me.jackstar.drakesranks.manager.DrakesRanksManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DrakesRanksPlaceholderExpansion extends PlaceholderExpansion {

    private final DrakesRanksManager ranksManager;

    public DrakesRanksPlaceholderExpansion(DrakesRanksManager ranksManager) {
        this.ranksManager = ranksManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "drakesranks";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JackStar";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        Rank rank = ranksManager.getPlayerRank(player);
        if (rank == null) {
            return "";
        }

        if ("rank".equalsIgnoreCase(params) || "name".equalsIgnoreCase(params)) {
            return rank.getName();
        }
        if ("prefix".equalsIgnoreCase(params)) {
            return rank.getPrefix();
        }
        if ("suffix".equalsIgnoreCase(params)) {
            return rank.getSuffix();
        }
        if ("color".equalsIgnoreCase(params)) {
            return rank.getColor();
        }
        if ("weight".equalsIgnoreCase(params)) {
            return String.valueOf(rank.getWeight());
        }

        return null;
    }
}
