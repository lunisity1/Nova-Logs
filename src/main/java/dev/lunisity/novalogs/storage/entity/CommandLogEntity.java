package dev.lunisity.novalogs.storage.entity;

import com.google.gson.annotations.Expose;
import dev.lunisity.aurora.json.entity.impl.BasicStorageEntity;
import dev.lunisity.borealis.text.time.TimeUtil;
import dev.lunisity.novalogs.data.abstracts.LogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.ChatLogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.CommandLogEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommandLogEntity extends BasicStorageEntity {

    @Expose
    private final List<CommandLogEntry> commandLogs = new ArrayList<>();

    public CommandLogEntity(UUID key) {
        super(key);
    }

    public List<CommandLogEntry> getLogs() {
        List<CommandLogEntry> logs = new ArrayList<>();
        for (CommandLogEntry entry : commandLogs) {
            if (entry.isExpired()) {
                commandLogs.remove(entry);
                continue;
            }
            logs.add(entry);
        }
        return logs;
    }

    public List<CommandLogEntry> searchCommand(final String command) {
        List<CommandLogEntry> logs = new ArrayList<>();
        for (CommandLogEntry entry : commandLogs) {
            if (!entry.getCommand().equals(command)) {
                continue;
            }
            logs.add(entry);
        }
        return logs;
    }

    public List<CommandLogEntry> searchDate(String date) {
        List<CommandLogEntry> result = new ArrayList<>();
        long startMillis;
        long endMillis;


        startMillis = TimeUtil.getStartOfDayMillis(date);
        endMillis = startMillis + TimeUnit.DAYS.toMillis(1);

        if (startMillis == 0 || endMillis == 0) {
            return new ArrayList<>();
        }

        for (CommandLogEntry entry : commandLogs) {
            long entryTime = entry.getTime();
            if (entryTime >= startMillis && entryTime <= endMillis) {
                result.add(entry);
            }
        }
        return result;
    }

    public List<CommandLogEntry> searchTime(String date, String startTime, String endTime) {
        List<CommandLogEntry> result = new ArrayList<>();

        final long startOfDay = TimeUtil.getStartOfDayMillis(date);

        long startMillis = TimeUtil.getMillisFrom24HourTime(startTime) + startOfDay;
        long endMillis = TimeUtil.getMillisFrom24HourTime(endTime) + startOfDay;

        if (startMillis == 0 || endMillis == 0) {
            return new ArrayList<>();
        }

        for (CommandLogEntry entry : commandLogs) {
            long entryTime = entry.getTime();
            if (entryTime >= startMillis && entryTime <= endMillis) {
                result.add(entry);
            }
        }

        return result;
    }

    public void addEntry(final String command) {
        this.commandLogs.add(new CommandLogEntry(command));
    }

    public void addEntry(final String command, final long time) {
        this.commandLogs.add(new CommandLogEntry(command, time));
    }

    public void clearEntries() {
        commandLogs.clear();
    }

    public void clearExpired() {
        commandLogs.removeIf(LogEntry::isExpired);
    }

}
