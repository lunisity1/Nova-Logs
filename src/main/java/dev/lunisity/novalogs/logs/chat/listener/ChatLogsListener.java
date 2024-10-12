package dev.lunisity.novalogs.logs.chat.listener;

import dev.lunisity.novalogs.NovaLogs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChatLogsListener implements Listener {

    private final NovaLogs novaLogs;

    public ChatLogsListener(final NovaLogs novaLogs) {
        this.novaLogs = novaLogs;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        if (message.startsWith("/")) return;

        this.novaLogs.getChatLogsManager().addEntry(player, message);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.novaLogs.getChatLogsManager().clearExpired(player);
    }

}