package dev.lunisity.novalogs.commands.impl.command.search;

import dev.lunisity.novacommands.abstracts.SubCommand;
import dev.lunisity.novacommands.context.CommandContext;
import dev.lunisity.novalogs.NovaLogs;
import org.bukkit.entity.Player;

public class SearchCommand extends SubCommand {

    private final NovaLogs novaLogs;

    public SearchCommand(final NovaLogs novaLogs) {
        super("search");
        this.novaLogs = novaLogs;
    }


    @Override
    public void execute(CommandContext commandContext) {
        final Player player = (Player) commandContext.getSender();

        if (!player.hasPermission("novalogs.command")) {
            this.novaLogs.getMessageCache().sendMessage(player, "NO-PERMISSION");
            return;
        }

        if (commandContext.getLength() < 3) {
            this.novaLogs.getMessageCache().sendMessage(player, "SEARCH-INVALID-USAGE");
            return;
        }

        if (!commandContext.isPlayer(1)) {
            this.novaLogs.getMessageCache().sendMessage(player, "INVALID-PLAYER");
            return;
        }

        final Player target = commandContext.asPlayer(1);

        if (commandContext.getLength() == 3) {
            this.novaLogs.getCommandLogsManager().searchCommand(player, target, commandContext.get(2), 1);
            return;
        }

        if (commandContext.getLength() == 4) {
            this.novaLogs.getCommandLogsManager().searchCommand(player, target, commandContext.get(2), commandContext.asInt(3));
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(player, "SEARCH-INVALID-USAGE");
    }
}
