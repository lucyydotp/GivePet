package me.lucyydotp.givepet;

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

        plugin.transferManager().setEntity(e.getPlayer(), target);
    }
}
