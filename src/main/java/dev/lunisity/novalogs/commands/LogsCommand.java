package dev.lunisity.novalogs.commands;

import dev.lunisity.novacommands.abstracts.AbstractCommand;
import dev.lunisity.novacommands.context.CommandContext;
import dev.lunisity.novalogs.NovaLogs;
import dev.lunisity.novalogs.commands.impl.chat.ChatLogsCommand;
import dev.lunisity.novalogs.commands.impl.command.CommandLogsCommand;
import dev.lunisity.novalogs.commands.impl.voucher.VoucherLogsCommand;
import org.bukkit.entity.Player;

public class LogsCommand extends AbstractCommand<NovaLogs> {

    private final NovaLogs novaLogs;

    public LogsCommand(final NovaLogs novaLogs) {
        super("logs");
        this.novaLogs = novaLogs;

        withSubCommand(
                new ChatLogsCommand(novaLogs),
                new VoucherLogsCommand(novaLogs),
                new CommandLogsCommand(novaLogs)
        );
    }

    // /logs <type> <player> <page>

    @Override
    protected void execute(CommandContext commandContext) {
        final Player player = (Player) commandContext.getSender();

        if (!player.hasPermission("novalogs.admin")) {
            this.novaLogs.getMessageCache().sendMessage(player, "NO-PERMISSION");
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(player, "INVALID-USAGE");
    }
}
