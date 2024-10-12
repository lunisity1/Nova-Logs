package dev.lunisity.novalogs.storage.creator;

import dev.lunisity.aurora.json.creator.InstanceCreator;
import dev.lunisity.novalogs.storage.entity.CommandLogEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandLogCreator implements InstanceCreator<CommandLogEntity> {

    @Override
    public CommandLogEntity create(UUID uuid) {
        return new CommandLogEntity(uuid);
    }

    @Override
    public boolean canCreate(UUID uuid) {
        return true;
    }

    @Override
    public boolean canUnload(CommandLogEntity commandLogEntity) {
        final UUID uuid = commandLogEntity.getKey();

        final Player player = Bukkit.getPlayer(uuid);

        return player == null || !player.isOnline();
    }

}
