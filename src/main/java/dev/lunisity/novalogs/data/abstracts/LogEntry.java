package dev.lunisity.novalogs.data.abstracts;

import dev.lunisity.novalogs.data.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class LogEntry {

    private final long time;
    private LogType logType;

    public LogEntry(final LogType logType) {
        this.time = System.currentTimeMillis();
        this.logType = logType;
    }

    public LogEntry(final LogType logType, final long time) {
        this.time = time;
        this.logType = logType;
    }

    public abstract long getExpiration();

    public boolean isExpired() {
        return System.currentTimeMillis() >= getExpiration();
    }
}