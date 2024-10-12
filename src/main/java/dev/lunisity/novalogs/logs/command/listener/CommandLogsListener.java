package dev.lunisity.novalogs.logs.command.listener;

import dev.lunisity.novalogs.NovaLogs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CommandLogsListener implements Listener {

    private final NovaLogs novaLogs;

    public CommandLogsListener(final NovaLogs novaLogs) {
        this.novaLogs = novaLogs;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.novaLogs.getCommandLogsManager().clearEntries(event.getPlayer());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        this.novaLogs.getCommandLogsManager().addEntry(event.getPlayer(), event.getMessage());
    }

}
