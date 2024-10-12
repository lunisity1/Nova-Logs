package dev.lunisity.novalogs.storage.creator;

import dev.lunisity.aurora.json.creator.InstanceCreator;
import dev.lunisity.novalogs.storage.entity.VoucherLogEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VoucherLogCreator implements InstanceCreator<VoucherLogEntity> {

    @Override
    public VoucherLogEntity create(UUID uuid) {
        return new VoucherLogEntity(uuid);
    }

    @Override
    public boolean canCreate(UUID uuid) {
        return true;
    }

    @Override
    public boolean canUnload(VoucherLogEntity voucherLogEntity) {
        final UUID uuid = voucherLogEntity.getKey();

        final Player player = Bukkit.getPlayer(uuid);

        return player == null || !player.isOnline();
    }

}
