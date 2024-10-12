package dev.lunisity.novalogs.data.abstracts.impl;

import com.google.gson.annotations.Expose;
import dev.lunisity.novalogs.data.abstracts.LogEntry;
import dev.lunisity.novalogs.data.enums.LogType;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class CommandLogEntry extends LogEntry {

    @Expose
    private final String command;

    public CommandLogEntry(final String command, final long time) {
        super(LogType.COMMAND, time);

        this.command = command;
    }

    public CommandLogEntry(final String command) {
        this(command, System.currentTimeMillis());
    }

    private static final long DURATION = TimeUnit.DAYS.toMillis(7);

    @Override
    public long getExpiration() {
        return getTime() + CommandLogEntry.DURATION;
    }
}
