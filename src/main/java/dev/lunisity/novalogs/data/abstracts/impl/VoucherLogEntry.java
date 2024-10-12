package dev.lunisity.novalogs.data.abstracts.impl;

import com.google.gson.annotations.Expose;
import dev.lunisity.novalogs.data.abstracts.LogEntry;
import dev.lunisity.novalogs.data.enums.LogType;
import dev.lunisity.novavouchers.data.Voucher;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class VoucherLogEntry extends LogEntry {

    @Expose
    private final Voucher voucher;

    public VoucherLogEntry(final Voucher voucher, final long time) {
        super(LogType.VOUCHER, time);

        this.voucher = voucher;
    }

    public VoucherLogEntry(final Voucher voucher) {
        this (voucher, System.currentTimeMillis());
    }

    private static final long DURATION = TimeUnit.DAYS.toMillis(14);

    @Override
    public long getExpiration() {
        return getTime() + VoucherLogEntry.DURATION;
    }
}
