package dev.lunisity.novalogs.logs.command.manager;

import dev.lunisity.borealis.text.TextJoiner;
import dev.lunisity.borealis.text.TextReplacer;
import dev.lunisity.borealis.text.time.TimeUtil;
import dev.lunisity.novalogs.NovaLogs;
import dev.lunisity.novalogs.data.abstracts.impl.ChatLogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.CommandLogEntry;
import dev.lunisity.novalogs.storage.entity.ChatLogEntity;
import dev.lunisity.novalogs.storage.entity.CommandLogEntity;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandLogsManager {

    private final NovaLogs novaLogs;

    public CommandLogsManager(final NovaLogs novaLogs) {
        this.novaLogs = novaLogs;
    }

    public void addEntry(final Player player, final String command) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(player.getUniqueId());

        entity.addEntry(command);
        entity.setModified(true);

        this.novaLogs.getCommandStorageManager().write(entity);
    }

    public void clearEntries(final Player player) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(player.getUniqueId());

        entity.clearEntries();
        entity.setModified(true);

        this.novaLogs.getCommandStorageManager().write(entity);
    }

    public void clearExpired(final Player player) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(player.getUniqueId());

        entity.clearExpired();
        entity.setModified(true);

        this.novaLogs.getCommandStorageManager().write(entity);
    }

    public int getMaxPages(final OfflinePlayer target) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(target.getUniqueId());

        List<CommandLogEntry> entries = entity.getLogs();
        final int entriesPerPage = 10;

        return (int) Math.ceil((double) entries.size() / entriesPerPage);
    }

    public void sendLogs(final Player executor, final OfflinePlayer target, final int page) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(target.getUniqueId());

        List<CommandLogEntry> entries = entity.getLogs();
        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 issued &f%command% &7(%time%)";

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        List<CommandLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        for (final CommandLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%command%", entry.getCommand());
            replacer.with("%time%", entry.getTime());

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "COMMAND-LOGS-FOUND", replacer);
    }

    public void searchCommand(final Player executor, final OfflinePlayer target, final String command, final int page) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(target.getUniqueId());

        List<CommandLogEntry> entries = entity.searchCommand(command);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%command%", command);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS-WITH-COMMAND", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "SEARCHING-COMMAND", new TextReplacer()
                .with("%player%", target.getName())
                .with("%command%", command));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 issued &f%command% &7(%time%)";

        List<CommandLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final CommandLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%command%", entry.getCommand());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "COMMAND-LOGS-FOUND", replacer);
    }

    public void searchDate(final Player executor, final OfflinePlayer target, final String date, final int page) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(target.getUniqueId());

        List<CommandLogEntry> entries = entity.searchDate(date);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%date%", date);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS-ON-DATE", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "COMMAND-SEARCHING-DATE", new TextReplacer()
                .with("%player%", target.getName())
                .with("%date%", date));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 issued &f%command% &7(%time%)";

        List<CommandLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final CommandLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%command%", entry.getCommand());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "COMMAND-LOGS-FOUND", replacer);
    }

    public void searchTime(final Player executor, final OfflinePlayer target, final String date, final String startTime, final String endTime, final int page) {
        CommandLogEntity entity = this.novaLogs.getCommandStorageManager().read(target.getUniqueId());

        List<CommandLogEntry> entries = entity.searchTime(date, startTime, endTime);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%date%", date)
                    .with("%startTime%", startTime)
                    .with("%endTime%", endTime);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS-IN-TIME", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-COMMAND-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "COMMAND-SEARCHING-TIME", new TextReplacer()
                .with("%player%", target.getName())
                .with("%date%", date)
                .with("%startTime%", startTime)
                .with("%endTime%", endTime));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 issued &f%command% &7(%time%)";

        List<CommandLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final CommandLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%command%", entry.getCommand());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "COMMAND-LOGS-FOUND", replacer);
    }

}
