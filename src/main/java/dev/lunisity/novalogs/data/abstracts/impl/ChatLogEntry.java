package dev.lunisity.novalogs.data.abstracts.impl;

import com.google.gson.annotations.Expose;
import dev.lunisity.novalogs.data.abstracts.LogEntry;
import dev.lunisity.novalogs.data.enums.LogType;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class ChatLogEntry extends LogEntry {

    @Expose
    private final String message;

    public ChatLogEntry(final String message, final long time) {
        super(LogType.CHAT, time);

        this.message = message;
    }

    public ChatLogEntry(final String message) {
        this(message, System.currentTimeMillis());
    }

    private static final long DURATION = TimeUnit.DAYS.toMillis(7);

    @Override
    public long getExpiration() {
        return getTime() + ChatLogEntry.DURATION;
    }
}