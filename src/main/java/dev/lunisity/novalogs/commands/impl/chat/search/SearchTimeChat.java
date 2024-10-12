package dev.lunisity.novalogs.commands.impl.chat.search;

import dev.lunisity.novacommands.abstracts.SubCommand;
import dev.lunisity.novacommands.context.CommandContext;
import dev.lunisity.novalogs.NovaLogs;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SearchTimeChat extends SubCommand {

    private final NovaLogs novaLogs;

    public SearchTimeChat(final NovaLogs novaLogs) {
        super("chat-time");
        this.novaLogs = novaLogs;
    }

    @Override
    public void execute(CommandContext commandContext) {
        final Player player = (Player) commandContext.getSender();

        if (!player.hasPermission("novalogs.chat")) {
            this.novaLogs.getMessageCache().sendMessage(player, "NO-PERMISSION");
            return;
        }

        if (commandContext.getLength() < 5) {
            this.novaLogs.getMessageCache().sendMessage(player, "SEARCH-INVALID-USAGE");
            return;
        }

        if (!commandContext.isPlayer(1)) {
            this.novaLogs.getMessageCache().sendMessage(player, "INVALID-PLAYER");
            return;
        }

        final OfflinePlayer target = commandContext.asPlayer(1);

        if (commandContext.getLength() == 5) {
            this.novaLogs.getChatLogsManager().searchTime(player, target, commandContext.get(2), commandContext.get(3), commandContext.get(4), 1);
            return;
        }

        if (commandContext.getLength() == 6) {
            this.novaLogs.getChatLogsManager().searchTime(player, target, commandContext.get(2), commandContext.get(3), commandContext.get(4), commandContext.asInt(5));
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(player, "SEARCH-INVALID-USAGE");
    }
}
