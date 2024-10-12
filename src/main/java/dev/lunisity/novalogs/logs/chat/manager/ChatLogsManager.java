package dev.lunisity.novalogs.logs.chat.manager;

import dev.lunisity.borealis.text.TextJoiner;
import dev.lunisity.borealis.text.TextReplacer;
import dev.lunisity.borealis.text.time.TimeUtil;
import dev.lunisity.novalogs.NovaLogs;
import dev.lunisity.novalogs.data.abstracts.impl.ChatLogEntry;
import dev.lunisity.novalogs.storage.entity.ChatLogEntity;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatLogsManager {

    private final NovaLogs novaLogs;

    public ChatLogsManager(final NovaLogs novaLogs) {
        this.novaLogs = novaLogs;
    }

    public void addEntry(final Player player, final String message) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(player.getUniqueId());

        entity.addEntry(message);
        entity.setModified(true);

        this.novaLogs.getChatStorageManager().write(entity);
    }

    public void clearEntries(final Player player) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(player.getUniqueId());

        entity.clearEntries();
        entity.setModified(true);

        this.novaLogs.getChatStorageManager().write(entity);
    }

    public void clearExpired(final Player player) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(player.getUniqueId());

        entity.clearExpired();
        entity.setModified(true);

        this.novaLogs.getChatStorageManager().write(entity);
    }

    public int getMaxPages(final OfflinePlayer target) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(target.getUniqueId());

        List<ChatLogEntry> entries = entity.getLogs();
        final int entriesPerPage = 10;

        return (int) Math.ceil((double) entries.size() / entriesPerPage);
    }

    public void sendLogs(final Player executor, final OfflinePlayer target, final int page) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(target.getUniqueId());

        List<ChatLogEntry> entries = entity.getLogs();
        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-CHAT-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7: &f%message% &7(%time%)";

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        List<ChatLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        for (final ChatLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%message%", entry.getMessage());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "CHAT-LOGS-FOUND", replacer);
    }

    public void searchMessage(final Player executor, final OfflinePlayer target, final String message, final int page) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(target.getUniqueId());

        List<ChatLogEntry> entries = entity.searchMessage(message);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%message%", message);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-LOGS-IN-MESSAGE", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-CHAT-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "SEARCHING-MESSAGE", new TextReplacer()
                .with("%player%", target.getName())
                .with("%message%", message));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7: &f%message% &7(%time%)";

        List<ChatLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final ChatLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%message%", entry.getMessage());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "CHAT-LOGS-FOUND", replacer);
    }

    public void searchDate(final Player executor, final OfflinePlayer target, final String date, final int page) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(target.getUniqueId());

        List<ChatLogEntry> entries = entity.searchDate(date);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%date%", date);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-CHAT-LOGS-ON-DATE", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "CHAT-SEARCHING-DATE", new TextReplacer()
                .with("%player%", target.getName())
                .with("%date%", date));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7: &f%message% &7(%time%)";

        List<ChatLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final ChatLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%message%", entry.getMessage());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "CHAT-LOGS-FOUND", replacer);
    }

    public void searchTime(final Player executor, final OfflinePlayer target, final String date, final String startTime, final String endTime, final int page) {
        ChatLogEntity entity = this.novaLogs.getChatStorageManager().read(target.getUniqueId());

        List<ChatLogEntry> entries = entity.searchTime(date, startTime, endTime);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%date%", date)
                    .with("%startTime%", startTime)
                    .with("%endTime%", endTime);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-CHAT-LOGS-IN-TIME", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-CHAT-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "CHAT-SEARCHING-TIME", new TextReplacer()
                .with("%player%", target.getName())
                .with("%date%", date)
                .with("%startTime%", startTime)
                .with("%endTime%", endTime));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7: &f%message% &7(%time%)";

        List<ChatLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final ChatLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%message%", entry.getMessage());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "CHAT-LOGS-FOUND", replacer);
    }
}