package dev.lunisity.novalogs.logs.voucher.manager;

import dev.lunisity.borealis.text.TextJoiner;
import dev.lunisity.borealis.text.TextReplacer;
import dev.lunisity.borealis.text.time.TimeUtil;
import dev.lunisity.novalogs.NovaLogs;
import dev.lunisity.novalogs.data.abstracts.impl.ChatLogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.VoucherLogEntry;
import dev.lunisity.novalogs.storage.entity.ChatLogEntity;
import dev.lunisity.novalogs.storage.entity.VoucherLogEntity;
import dev.lunisity.novavouchers.NovaVouchers;
import dev.lunisity.novavouchers.data.Voucher;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoucherLogsManager {

    private final NovaLogs novaLogs;

    public VoucherLogsManager(final NovaLogs novaLogs) {
        this.novaLogs = novaLogs;
    }

    public void addEntry(final Player player, final Voucher voucher) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(player.getUniqueId());

        if (voucher == null) return;

        entity.addEntry(voucher);
        entity.setModified(true);

        this.novaLogs.getVoucherStorageManager().write(entity);
    }

    public void clearEntries(final Player player) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(player.getUniqueId());

        entity.clearEntries();
        entity.setModified(true);

        this.novaLogs.getVoucherStorageManager().write(entity);
    }

    public void clearExpired(final Player player) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(player.getUniqueId());

        entity.clearExpired();
        entity.setModified(true);

        this.novaLogs.getVoucherStorageManager().write(entity);
    }

    public int getMaxPages(final OfflinePlayer target) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(target.getUniqueId());

        List<VoucherLogEntry> entries = entity.getLogs();
        final int entriesPerPage = 10;

        return (int) Math.ceil((double) entries.size() / entriesPerPage);
    }

    public void sendLogs(final Player executor, final OfflinePlayer target, final int page) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(target.getUniqueId());

        List<VoucherLogEntry> entries = entity.getLogs();
        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-VOUCHER-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 redeemed &f%voucher% &7(%time%)";

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        List<VoucherLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        for (final VoucherLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%voucher%", entry.getVoucher().getName());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-LOGS-FOUND", replacer);
    }

    public void searchVoucher(final Player executor, final OfflinePlayer target, final String voucher, final int page) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(target.getUniqueId());

        List<VoucherLogEntry> entries = entity.searchVoucher(voucher);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%voucher%", voucher);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-VOUCHER-LOGS-WITH-VOUCHER", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-VOUCHER-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-SEARCHING-VOUCHER", new TextReplacer()
                .with("%player%", target.getName())
                .with("%voucher%", voucher));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 redeemed &f%voucher% &7(%time%)";

        List<VoucherLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final VoucherLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%voucher%", entry.getVoucher().getName());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-LOGS-FOUND", replacer);
    }

    public void searchDate(final Player executor, final OfflinePlayer target, final String date, final int page) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(target.getUniqueId());

        List<VoucherLogEntry> entries = entity.searchDate(date);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%date%", date);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-VOUCHER-LOGS-ON-DATE", replacer);
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

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-SEARCHING-DATE", new TextReplacer()
                .with("%player%", target.getName())
                .with("%date%", date));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 redeemed &f%voucher% &7(%time%)";

        List<VoucherLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final VoucherLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%voucher%", entry.getVoucher().getName());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-LOGS-FOUND", replacer);
    }

    public void searchTime(final Player executor, final OfflinePlayer target, final String date, final String startTime, final String endTime, final int page) {
        VoucherLogEntity entity = this.novaLogs.getVoucherStorageManager().read(target.getUniqueId());

        List<VoucherLogEntry> entries = entity.searchTime(date, startTime, endTime);

        if (entries.isEmpty()) {
            TextReplacer replacer = new TextReplacer()
                    .with("%player%", target.getName())
                    .with("%date%", date)
                    .with("%startTime%", startTime)
                    .with("%endTime%", endTime);
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-VOUCHER-LOGS-IN-TIME", replacer);
            return;
        }

        final int entriesPerPage = 10;
        final int startIndex = (page - 1) * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

        final int totalPages = (int) Math.ceil((double) entries.size() / entriesPerPage);

        if (totalPages == 0) {
            TextReplacer replacer = new TextReplacer().with("%player%", target.getName());
            this.novaLogs.getMessageCache().sendMessage(executor, "NO-VOUCHER-LOGS", replacer);
            return;
        }

        if (page > totalPages) {
            TextReplacer replacer = new TextReplacer().with("%maxPages%", totalPages);
            this.novaLogs.getMessageCache().sendMessage(executor, "INVALID-PAGE", replacer);
            return;
        }

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-SEARCHING-TIME", new TextReplacer()
                .with("%player%", target.getName())
                .with("%date%", date)
                .with("%startTime%", startTime)
                .with("%endTime%", endTime));

        final TextJoiner entriesJoiner = new TextJoiner();

        String format = " &a%player%&7 redeemed &f%voucher% &7(%time%)";

        List<VoucherLogEntry> reversedList = new ArrayList<>(entries);
        Collections.reverse(reversedList);

        TextReplacer replacer = new TextReplacer()
                .with("%player%", target.getName())
                .with("%page%", page)
                .with("%maxPages%", totalPages);

        for (final VoucherLogEntry entry : reversedList.subList(startIndex, endIndex)) {
            replacer.with("%voucher%", entry.getVoucher().getName());
            replacer.with("%time%", TimeUtil.getTimeStamp(entry.getTime()));

            entriesJoiner.appendLine(replacer.apply(format));
        }

        replacer.with("%logs%", entriesJoiner.toString());

        this.novaLogs.getMessageCache().sendMessage(executor, "VOUCHER-LOGS-FOUND", replacer);
    }

}
