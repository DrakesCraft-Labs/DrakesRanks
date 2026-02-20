package me.jackstar.drakesranks.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.jackstar.drakescraft.utils.MessageUtils;
import me.jackstar.drakescraft.utils.PlaceholderUtils;
import me.jackstar.drakesranks.domain.Rank;
import me.jackstar.drakesranks.manager.DrakesRanksManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DrakesRanksListener implements Listener {

    private final DrakesRanksManager ranksManager;

    public DrakesRanksListener(DrakesRanksManager ranksManager) {
        this.ranksManager = ranksManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent event) {
        ranksManager.applyPermissions(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ranksManager.applyPermissions(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ranksManager.clearAttachment(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(AsyncChatEvent event) {
        Rank rank = ranksManager.getPlayerRank(event.getPlayer());
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            String prefix = PlaceholderUtils.applyPlaceholders(source, rank.getPrefix());
            String suffix = PlaceholderUtils.applyPlaceholders(source, rank.getSuffix());

            TextComponent.Builder builder = Component.text();
            builder.append(MessageUtils.parse(prefix));
            builder.append(Component.space());
            builder.append(Component.text("<"));
            builder.append(sourceDisplayName);
            builder.append(Component.text("> "));
            builder.append(message);
            if (suffix != null && !suffix.isBlank()) {
                builder.append(Component.space());
                builder.append(MessageUtils.parse(suffix));
            }
            return builder.build();
        });
    }
}
