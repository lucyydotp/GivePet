package me.lucyy.givepet;

import org.bukkit.Bukkit;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractListener implements Listener {

    private final GivePet plugin;

    public InteractListener(GivePet plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerInteractEntityEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;

        if (!(e.getRightClicked() instanceof Tameable target)) {
            return;
        }
        final var uuid = e.getPlayer().getUniqueId();

        TransferAttempt foundAttempt = plugin.transferAttempts().get(uuid);
        if (foundAttempt == null) return;

        e.setCancelled(true);

        AnimalTamer owner = target.getOwner();
        if (owner != null && owner.getUniqueId().equals(uuid)) {
            Player newOwner = Bukkit.getPlayer(foundAttempt.receiver());
            if (newOwner == null) {
                e.getPlayer().sendMessage(plugin.getMsg("playerLeft"));
                plugin.transferAttempts().remove(foundAttempt);
                return;
            }
            target.setOwner(newOwner);
            target.teleport(newOwner);
            newOwner.sendMessage(plugin.getMsg("sentReceiverMsg")
                    .replace("{sender}", e.getPlayer().getDisplayName())
                    .replace("{type}", target.getType().toString().toLowerCase())
            );
            e.getPlayer().sendMessage(plugin.getMsg("sentSenderMsg"));
        } else {
            e.getPlayer().sendMessage(plugin.getMsg("notOwned"));
        }

        plugin.transferAttempts().remove(uuid);
    }
}
