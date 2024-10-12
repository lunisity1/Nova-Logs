package dev.lunisity.novalogs.commands.impl.command;

import dev.lunisity.borealis.text.TextReplacer;
import dev.lunisity.novacommands.abstracts.SubCommand;
import dev.lunisity.novacommands.context.CommandContext;
import dev.lunisity.novalogs.NovaLogs;
import org.bukkit.entity.Player;

public class CommandLogsCommand extends SubCommand {

    private final NovaLogs novaLogs;

    public CommandLogsCommand(final NovaLogs novaLogs) {
        super("command");
        this.novaLogs = novaLogs;
    }

    @Override
    public void execute(CommandContext commandContext) {
        final Player player = (Player) commandContext.getSender();

        if (!player.hasPermission("novalogs.command")) {
            this.novaLogs.getMessageCache().sendMessage(player, "NO-PERMISSION");
            return;
        }

        if (commandContext.getLength() < 2) {
            this.novaLogs.getMessageCache().sendMessage(player, "INVALID-USAGE");
            return;
        }

        if (!commandContext.isPlayer(1) && !commandContext.isOfflinePlayer(1)) {
            TextReplacer replacer = new TextReplacer().with("%player%", commandContext.get(1));
            this.novaLogs.getMessageCache().sendMessage(player, "INVALID-PLAYER", replacer);
            return;
        }

        final Player target = commandContext.asPlayer(1);

        if (commandContext.getLength() == 2) {
            this.novaLogs.getCommandLogsManager().sendLogs(player, target, 1);
            return;
        }

        if (!commandContext.isInt(2)) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", this.novaLogs.getChatLogsManager().getMaxPages(target));
            this.novaLogs.getMessageCache().sendMessage(player, "INVALID-PAGE", replacer);
            return;
        }

        final int page = commandContext.asInt(2);

        this.novaLogs.getCommandLogsManager().sendLogs(player, target, page);
    }
}
