package dev.lunisity.novalogs;

import dev.lunisity.aurora.json.cache.impl.MapStorageCache;
import dev.lunisity.aurora.json.manager.StorageManager;
import dev.lunisity.aurora.json.manager.impl.StorageManagerImpl;
import dev.lunisity.aurora.json.source.DataSource;
import dev.lunisity.aurora.json.source.impl.FileDataSource;
import dev.lunisity.borealis.component.Component;
import dev.lunisity.borealis.json.JsonUtils;
import dev.lunisity.novaconfig.interfaces.Config;
import dev.lunisity.novaconfig.manager.FileManager;
import dev.lunisity.novalogs.logs.chat.listener.ChatLogsListener;
import dev.lunisity.novalogs.logs.chat.manager.ChatLogsManager;
import dev.lunisity.novalogs.commands.LogsCommand;
import dev.lunisity.novalogs.commands.SearchLogsCommand;
import dev.lunisity.novalogs.logs.command.listener.CommandLogsListener;
import dev.lunisity.novalogs.logs.command.manager.CommandLogsManager;
import dev.lunisity.novalogs.logs.voucher.listener.VoucherLogsListener;
import dev.lunisity.novalogs.logs.voucher.manager.VoucherLogsManager;
import dev.lunisity.novalogs.storage.creator.ChatLogCreator;
import dev.lunisity.novalogs.storage.creator.CommandLogCreator;
import dev.lunisity.novalogs.storage.creator.VoucherLogCreator;
import dev.lunisity.novalogs.storage.entity.ChatLogEntity;
import dev.lunisity.novalogs.storage.entity.CommandLogEntity;
import dev.lunisity.novalogs.storage.entity.VoucherLogEntity;
import dev.lunisity.novalogs.util.LogUtils;
import dev.lunisity.novamessages.messages.MessageCache;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class NovaLogs extends JavaPlugin implements Component<JavaPlugin> {

    private static NovaLogs INSTANCE;

    public FileManager fileManager;
    private MessageCache messageCache;

    private Config messageConfig;

    private DataSource<ChatLogEntity> chatSource;
    private StorageManager<ChatLogEntity> chatStorageManager;

    private DataSource<VoucherLogEntity> voucherSource;
    private StorageManager<VoucherLogEntity> voucherStorageManager;

    private DataSource<CommandLogEntity> commandSource;
    private StorageManager<CommandLogEntity> commandStorageManager;

    private ChatLogsManager chatLogsManager;
    private VoucherLogsManager voucherLogsManager;
    private CommandLogsManager commandLogsManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LogUtils.logger.info("NovaLogs enabled!");

        this.fileManager = new FileManager(this);

        this.messageConfig = this.fileManager.load("messages.yml");
        this.messageCache = new MessageCache(messageConfig, "Messages");

        File chatLogsFolder = this.fileManager.createStorageFolder("/chat");
        this.chatSource = new FileDataSource<>(chatLogsFolder, ChatLogEntity.class, JsonUtils.GSON);
        this.chatStorageManager = new StorageManagerImpl<>(chatSource, new MapStorageCache<>(), new ChatLogCreator());

        File voucherLogsFolder = this.fileManager.createStorageFolder("/vouchers");
        this.voucherSource = new FileDataSource<>(voucherLogsFolder, VoucherLogEntity.class, JsonUtils.GSON);
        this.voucherStorageManager = new StorageManagerImpl<>(voucherSource, new MapStorageCache<>(), new VoucherLogCreator());

        File commandLogsFolder = this.fileManager.createStorageFolder("/commands");
        this.commandSource = new FileDataSource<>(commandLogsFolder, CommandLogEntity.class, JsonUtils.GSON);
        this.commandStorageManager = new StorageManagerImpl<>(commandSource, new MapStorageCache<>(), new CommandLogCreator());

        this.chatLogsManager = new ChatLogsManager(this);
        Bukkit.getPluginManager().registerEvents(new ChatLogsListener(this), this);

        this.voucherLogsManager = new VoucherLogsManager(this);
        Bukkit.getPluginManager().registerEvents(new VoucherLogsListener(this), this);

        this.commandLogsManager = new CommandLogsManager(this);
        Bukkit.getPluginManager().registerEvents(new CommandLogsListener(this), this);

        registerCommands();
    }

    public void registerCommands() {
        new LogsCommand(this).bind(this);
        new SearchLogsCommand(this).bind(this);
    }

    public static NovaLogs get() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        this.chatStorageManager.save();
        LogUtils.logger.info("NovaLogs disabled!");
    }

    @Override
    public JavaPlugin getLoader() {
        return this;
    }
}
