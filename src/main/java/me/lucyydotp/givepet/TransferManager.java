package me.lucyydotp.givepet;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages pet transfers between players.
 */
public class TransferManager {

    /**
     * A map of sender player UUID to recipient player UUID for ongoing transfers.
     */
    private final Map<UUID, UUID> openTransfers = new HashMap<>();


    /**
     * A map of sender player UUID to pet entity UUID for transfers that need to be accepted by the recipient.
     */
    private final Map<UUID, UUID> entities = new HashMap<>();

    private final boolean requireAccept;

    public TransferManager(boolean requireAccept) {
        this.requireAccept = requireAccept;
    }

    /**
     * Starts a transfer of a pet between two players.
     *
     * @param sender    the player sending the pet
     * @param recipient the player receiving the pet
     */
    public void startTransfer(Player sender, Player recipient) {
        if (sender.getUniqueId().equals(recipient.getUniqueId())) {
            sender.sendMessage(Message.FAIL_SELF.text());
            return;
        }

        final var currentTransferPlayer = openTransfers.get(sender.getUniqueId());
        if (currentTransferPlayer != null) {
            sender.sendMessage(Message.FAIL_ALREADY_GIVING.text(
                    Placeholder.component("player", Bukkit.getPlayer(currentTransferPlayer).displayName())
            ));
            return;
        }

        sender.sendMessage(Message.START.text());
        openTransfers.put(sender.getUniqueId(), recipient.getUniqueId());
    }

    /**
     * Sets an entity UUID to transfer, if the sender is currently transferring a pet.
     *
     * @param sender the sender UUID
     * @param pet    the pet entity
     */
    public void setEntity(Player sender, Tameable pet) {

        final var recipientId = openTransfers.get(sender.getUniqueId());
        if (recipientId == null) return;

        if (entities.containsKey(sender.getUniqueId())) return;

        if (!sender.getUniqueId().equals(pet.getOwnerUniqueId())) {
            sender.sendMessage(Message.FAIL_NOT_OWNED.text());
            clean(sender.getUniqueId());
            return;
        }

        final var recipient = Bukkit.getPlayer(recipientId);
        if (recipient == null) {
            sender.sendMessage(Message.FAIL_PLAYER_LEFT.text());
            clean(sender.getUniqueId());
            return;
        }

        if (requireAccept) {
            entities.put(sender.getUniqueId(), pet.getUniqueId());
            sender.sendMessage(Message.ACCEPT_SENDER.text(
                    Placeholder.component("player", recipient.displayName()),
                    Placeholder.component("pet", pet.name())
            ));
            recipient.sendMessage(Message.ACCEPT_RECIPIENT.text(
                    Placeholder.component("player", recipient.displayName()),
                    Placeholder.unparsed("playername", recipient.getName()),
                    Placeholder.component("pet", pet.name())
            ));
        } else {
            transfer(pet, sender, recipient);
        }
    }

    public void cancelTransfer(Player sender) {
        final var recipient = openTransfers.get(sender.getUniqueId());
        if (recipient == null) {
            sender.sendMessage(Message.CANCEL_FAIL.text());
            return;
        }

        clean(sender.getUniqueId());
        sender.sendMessage(Message.CANCEL_SUCCESS_SENDER.text());

        // if the recipient is online, send them a message
        final var recipientPlayer = Bukkit.getPlayer(recipient);
        if (recipientPlayer == null) return;

        recipientPlayer.sendMessage(Message.CANCEL_SUCCESS_RECIPIENT.text(
                Placeholder.component("player", sender.displayName())
        ));
    }

    public void acceptTransfer(Player sender, Player recipient) {
        final var petId = entities.get(sender.getUniqueId());
        if (petId == null || !recipient.getUniqueId().equals(openTransfers.get(sender.getUniqueId()))) {
            recipient.sendMessage(Message.FAIL_NO_TRANSFER.text(
                    Placeholder.component("player", sender.displayName())
            ));
            return;
        }

        final var pet = Bukkit.getEntity(petId);
        if (pet == null) {
            recipient.sendMessage(Message.FAIL_DIED.text());
            return;
        }

        transfer((Tameable) pet, sender, recipient);
    }

    public void denyTransfer(Player sender, Player recipient) {
        if (!openTransfers.containsKey(sender.getUniqueId())) {
            recipient.sendMessage(Message.FAIL_NO_TRANSFER.text(
                    Placeholder.component("player", sender.displayName())
            ));
            return;
        }
        clean(sender.getUniqueId());

        sender.sendMessage(Message.DENY_SENDER.text(
                Placeholder.component("player", recipient.displayName())
        ));

        recipient.sendMessage(Message.DENY_RECIPIENT.text(
                Placeholder.component("player", sender.displayName())
        ));
    }

    private void clean(UUID sender) {
        openTransfers.remove(sender);
        entities.remove(sender);
    }

    /**
     * Transfers ownership of, and teleports, a pet to a new owner.
     * Also sends a message to the old and new owners confirming the transfer.
     *
     * @param pet      the pet
     * @param newOwner the new owner
     */
    private void transfer(Tameable pet, Player oldOwner, Player newOwner) {
        pet.setOwner(newOwner);
        pet.teleport(newOwner);
        clean(oldOwner.getUniqueId());

        final var petPlaceholder = Placeholder.component("pet", pet.name());
        oldOwner.sendMessage(Message.SENT_SENDER.text(
                Placeholder.component("player", newOwner.displayName()),
                petPlaceholder
        ));

        newOwner.sendMessage(Message.SENT_RECIPIENT.text(
                Placeholder.component("player", oldOwner.displayName()),
                petPlaceholder
        ));
    }
}
