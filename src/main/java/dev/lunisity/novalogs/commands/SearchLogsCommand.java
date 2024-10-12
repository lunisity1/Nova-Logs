package dev.lunisity.novalogs.commands;

import dev.lunisity.novacommands.abstracts.AbstractCommand;
import dev.lunisity.novacommands.context.CommandContext;
import dev.lunisity.novalogs.NovaLogs;
import dev.lunisity.novalogs.commands.impl.chat.search.SearchDateChat;
import dev.lunisity.novalogs.commands.impl.chat.search.SearchMessageChat;
import dev.lunisity.novalogs.commands.impl.chat.search.SearchTimeChat;
import dev.lunisity.novalogs.commands.impl.command.search.SearchCommand;
import dev.lunisity.novalogs.commands.impl.command.search.SearchDateCommand;
import dev.lunisity.novalogs.commands.impl.command.search.SearchTimeCommand;
import dev.lunisity.novalogs.commands.impl.voucher.search.SearchDateVoucher;
import dev.lunisity.novalogs.commands.impl.voucher.search.SearchTimeVoucher;
import dev.lunisity.novalogs.commands.impl.voucher.search.SearchVoucher;
import org.bukkit.entity.Player;

public class SearchLogsCommand extends AbstractCommand<NovaLogs> {

    private final NovaLogs novaLogs;

    public SearchLogsCommand(final NovaLogs novaLogs) {
        super("searchlogs");
        this.novaLogs = novaLogs;

        withSubCommand(
                new SearchDateChat(novaLogs),
                new SearchMessageChat(novaLogs),
                new SearchTimeChat(novaLogs),

                new SearchVoucher(novaLogs),
                new SearchDateVoucher(novaLogs),
                new SearchTimeVoucher(novaLogs),

                new SearchCommand(novaLogs),
                new SearchDateCommand(novaLogs),
                new SearchTimeCommand(novaLogs)
        );
    }

    @Override
    protected void execute(CommandContext commandContext) {
        final Player player = (Player) commandContext.getSender();

        if (!player.hasPermission("novalogs.admin")) {
            this.novaLogs.getMessageCache().sendMessage(player, "NO-PERMISSION");
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(player, "SEARCH-INVALID-USAGE");
    }
}
