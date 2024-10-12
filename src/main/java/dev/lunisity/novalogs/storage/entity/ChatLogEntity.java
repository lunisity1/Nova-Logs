package dev.lunisity.novalogs.storage.entity;

import com.google.gson.annotations.Expose;
import dev.lunisity.aurora.json.entity.impl.BasicStorageEntity;
import dev.lunisity.borealis.text.time.TimeUtil;
import dev.lunisity.novalogs.data.abstracts.LogEntry;
import dev.lunisity.novalogs.data.abstracts.impl.ChatLogEntry;
import dev.lunisity.novalogs.util.LogUtils;
import lombok.Getter;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class ChatLogEntity extends BasicStorageEntity {

    @Expose
    private final List<ChatLogEntry> chatLogs = new ArrayList<>();

    public ChatLogEntity(final UUID key) {
        super(key);
    }

    public List<ChatLogEntry> getLogs() {
        List<ChatLogEntry> logs = new ArrayList<>();
        for (ChatLogEntry entry : chatLogs) {
            if (entry.isExpired()) {
                chatLogs.remove(entry);
                continue;
            }
            logs.add(entry);
        }
        return logs;
    }

    public List<ChatLogEntry> searchMessage(final String message) {
        List<ChatLogEntry> logs = new ArrayList<>();
        for (ChatLogEntry entry : chatLogs) {
            if (!entry.getMessage().contains(message)) {
                continue;
            }
            logs.add(entry);
        }
        return logs;
    }

    public List<ChatLogEntry> searchDate(String date) {
        List<ChatLogEntry> result = new ArrayList<>();
        long startMillis;
        long endMillis;


        startMillis = TimeUtil.getStartOfDayMillis(date);
        endMillis = startMillis + TimeUnit.DAYS.toMillis(1);

        if (startMillis == 0 || endMillis == 0) {
            return new ArrayList<>();
        }

        for (ChatLogEntry entry : chatLogs) {
            long entryTime = entry.getTime();
            if (entryTime >= startMillis && entryTime <= endMillis) {
                result.add(entry);
            }
        }
        return result;
    }

    public List<ChatLogEntry> searchTime(String date, String startTime, String endTime) {
        List<ChatLogEntry> result = new ArrayList<>();

        final long startOfDay = TimeUtil.getStartOfDayMillis(date);

        long startMillis = TimeUtil.getMillisFrom24HourTime(startTime) + startOfDay;
        long endMillis = TimeUtil.getMillisFrom24HourTime(endTime) + startOfDay;

        if (startMillis == 0 || endMillis == 0) {
            return new ArrayList<>();
        }

        for (ChatLogEntry entry : chatLogs) {
            long entryTime = entry.getTime();
            if (entryTime >= startMillis && entryTime <= endMillis) {
                result.add(entry);
            }
        }

        return result;
    }

    public void addEntry(final String message) {
        this.chatLogs.add(new ChatLogEntry(message));
    }

    public void addEntry(final String message, final long time) {
        this.chatLogs.add(new ChatLogEntry(message, time));
    }

    public void clearEntries() {
        chatLogs.clear();
    }

    public void clearExpired() {
        chatLogs.removeIf(LogEntry::isExpired);
    }

}