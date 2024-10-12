package dev.lunisity.novalogs.storage.entity;

import com.google.gson.annotations.Expose;
import dev.lunisity.aurora.json.entity.impl.BasicStorageEntity;
import dev.lunisity.borealis.text.time.TimeUtil;
import dev.lunisity.novalogs.data.abstracts.LogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.ChatLogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.VoucherLogEntry;
import dev.lunisity.novavouchers.data.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class VoucherLogEntity extends BasicStorageEntity {

    @Expose
    private final List<VoucherLogEntry> voucherLogs = new ArrayList<>();

    public VoucherLogEntity(final UUID key) {
        super(key);
    }

    public List<VoucherLogEntry> getLogs() {
        List<VoucherLogEntry> logs = new ArrayList<>();
        for (VoucherLogEntry entry : voucherLogs) {
            if (entry.isExpired()) {
                voucherLogs.remove(entry);
                continue;
            }
            logs.add(entry);
        }
        return logs;
    }

    public List<VoucherLogEntry> searchVoucher(final String voucher) {
        List<VoucherLogEntry> logs = new ArrayList<>();
        for (VoucherLogEntry entry : voucherLogs) {
            if (!entry.getVoucher().getName().equals(voucher)) {
                continue;
            }
            logs.add(entry);
        }
        return logs;
    }

    public List<VoucherLogEntry> searchDate(final String date) {
        List<VoucherLogEntry> result = new ArrayList<>();
        long startMillis;
        long endMillis;

        startMillis = TimeUtil.getStartOfDayMillis(date);
        endMillis = startMillis + TimeUnit.DAYS.toMillis(1);

        if (startMillis == 0 || endMillis == 0) {
            return new ArrayList<>();
        }

        for (VoucherLogEntry entry : voucherLogs) {
            long entryTime = entry.getTime();
            if (entryTime >= startMillis && entryTime <= endMillis) {
                result.add(entry);
            }
        }
        return result;
    }

    public List<VoucherLogEntry> searchTime(final String date, final String startTime, final String endTime) {
        List<VoucherLogEntry> result = new ArrayList<>();

        final long startOfDay = TimeUtil.getStartOfDayMillis(date);

        long startMillis = TimeUtil.getMillisFrom24HourTime(startTime) + startOfDay;
        long endMillis = TimeUtil.getMillisFrom24HourTime(endTime) + startOfDay;

        if (startMillis == 0 || endMillis == 0) {
            return new ArrayList<>();
        }

        for (VoucherLogEntry entry : voucherLogs) {
            long entryTime = entry.getTime();
            if (entryTime >= startMillis && entryTime <= endMillis) {
                result.add(entry);
            }
        }

        return result;
    }

    public void addEntry(final Voucher voucher) {
        this.voucherLogs.add(new VoucherLogEntry(voucher));
    }

    public void addEntry(final Voucher voucher, final long time) {
        this.voucherLogs.add(new VoucherLogEntry(voucher, time));
    }

    public void clearEntries() {
        voucherLogs.clear();
    }

    public void clearExpired() {
        voucherLogs.removeIf(LogEntry::isExpired);
    }
}
