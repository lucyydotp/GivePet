package me.lucyydotp.givepet;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
                e.getPlayer().sendMessage(Message.FAIL_PLAYER_LEFT.text());
                plugin.transferAttempts().remove(uuid);
                return;
            }
            target.setOwner(newOwner);
            target.teleport(newOwner);

            newOwner.sendMessage(Message.SENT_RECEIVER.text(
                    Placeholder.component("player", e.getPlayer().displayName()),
                    Placeholder.component("pet", target.name())
            ));


            e.getPlayer().sendMessage(Message.SENT_SENDER.text(
                    Placeholder.component("player", newOwner.displayName()),
                    Placeholder.component("pet", target.name())
            ));
        } else {
            e.getPlayer().sendMessage(Message.FAIL_NOT_OWNED.text());
        }

        plugin.transferAttempts().remove(uuid);
    }
}
