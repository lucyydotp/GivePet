package me.lucyy.givepet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;


@RequiredArgsConstructor
public class InteractListener implements Listener {

	@NonNull private final GivePet plugin;

	@EventHandler
	public void on(PlayerInteractEntityEvent e) {
		if (e.getHand() != EquipmentSlot.HAND) return;
		if (e.getRightClicked() instanceof Tameable) {
			TransferAttempt foundAttempt = null;
			for (TransferAttempt attempt : plugin.getTransferAttempts()) {
				if (attempt.getGiver().equals(e.getPlayer().getUniqueId())) foundAttempt = attempt;
			}
			if (foundAttempt == null) return;

			e.setCancelled(true);

			Tameable target = (Tameable) e.getRightClicked();
			AnimalTamer owner = target.getOwner();
			if (owner != null && owner.getUniqueId().equals(e.getPlayer().getUniqueId())) {
				Player newOwner = Bukkit.getPlayer(foundAttempt.receiver);
				if (newOwner == null) {
					e.getPlayer().sendMessage(plugin.getMsg("playerLeft"));
					plugin.getTransferAttempts().remove(foundAttempt);
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
			plugin.getTransferAttempts().remove(foundAttempt);
		}
	}
}
