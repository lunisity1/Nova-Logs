package dev.lunisity.novalogs.logs.voucher.listener;

import dev.lunisity.novalogs.NovaLogs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VoucherLogsListener implements Listener {

    private final NovaLogs novaLogs;

    public VoucherLogsListener(final NovaLogs novaLogs) {
        this.novaLogs = novaLogs;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.novaLogs.getVoucherLogsManager().clearExpired(player);
    }

}
