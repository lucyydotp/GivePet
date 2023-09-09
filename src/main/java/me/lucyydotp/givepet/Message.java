package me.lucyydotp.givepet;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * A player-facing MiniMessage string.
 */
public enum Message {
    START("start", "<yellow>Please right click the pet you would like to give."),

    ACCEPT_SENDER("acceptSender", "<player> <yellow>has been offered your pet <white><pet></white>."),

    // todo: the embedded placeholders don't resolve
    ACCEPT_RECIPIENT("acceptRecipient",
            "<player> <yellow>has offered you their pet <white><pet></white>!\n" +
                    "<click:run_command:\"/givepet accept <playername>\"><green>[Accept]</click> " +
                    "<click:run_command:\"/givepet deny <playername>\"><red>[Deny]</click>"),

    SENT_SENDER("sendSender", "<green>You gave <white><player></white> your pet <white><pet></white>!"),
    SENT_RECIPIENT("sentRecipient", "<player><green> gave you a pet <white><pet></white>!"),
    FAIL_ALREADY_GIVING("alreadyGiving", "<red>You're already giving a pet to <white><player></white>!"),
    FAIL_PLAYER_NOT_FOUND("playerNotFound", "<red>The player <white><player></white> could not be found!"),
    FAIL_SELF("cannotGiveToSelf", "<red>You can't give a pet to yourself!"),
    FAIL_NOT_OWNED("notYourPet", "<red>That's not your pet!"),
    FAIL_PLAYER_LEFT("playerLeft", "<red>The player you were trying to give that pet to has since left the server."),
    FAIL_NO_TRANSFER("noTransfer", "<player><red> isn't giving you a pet!"),
    FAIL_DIED("died", "<red>The pet couldn't be found. It may no longer be alive."),
    CANCEL_SUCCESS_SENDER("cancelSuccess", "<green>Cancelled transferring a pet!"),
    CANCEL_SUCCESS_RECIPIENT("cancelRecipient", "<player><yellow> cancelled the transfer of their pet to you."),
    CANCEL_FAIL("cancelFail", "<red>You haven't tried to transfer a pet!"),
    DENY_SENDER("denySender", "<player><red> declined your pet."),
    DENY_RECIPIENT("denyRecipient", "<yellow>You declined <player>'s pet.");

    private final String key;
    private final String defaultMessage;

    Message(String key, String defaultMessage) {
        this.key = key;
        this.defaultMessage = defaultMessage;
    }

    /**
     * Converts this message to a MiniMessage component.
     */
    public Component text(TagResolver... entries) {
        return MiniMessage.miniMessage().deserialize(defaultMessage, entries);
    }
}
