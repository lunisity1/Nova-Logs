package dev.lunisity.novalogs.storage.creator;

import dev.lunisity.aurora.json.creator.InstanceCreator;
import dev.lunisity.novalogs.storage.entity.ChatLogEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatLogCreator implements InstanceCreator<ChatLogEntity> {

    @Override
    public ChatLogEntity create(UUID uuid) {
        return new ChatLogEntity(uuid);
    }

    @Override
    public boolean canCreate(UUID uuid) {
        return true;
    }

    @Override
    public boolean canUnload(ChatLogEntity chatLogEntity) {
        final UUID uuid = chatLogEntity.getKey();

        final Player player = Bukkit.getPlayer(uuid);

        return player == null || !player.isOnline();
    }

}