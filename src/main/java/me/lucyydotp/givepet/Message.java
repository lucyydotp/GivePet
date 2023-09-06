package me.lucyydotp.givepet;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * A player-facing MiniMessage string.
 */
public enum Message {
    START("start", "<yellow>Please right click the pet you would like to give."),
    SENT_SENDER("sentReceiver", "<green>You gave <white><player></white> your pet <white><pet></white>!"),
    SENT_RECEIVER("sentReceiver", "<player><green> gave you a pet <white><pet></white>!"),
    FAIL_PLAYER_NOT_FOUND("playerNotFound", "<red>The player <white><player></white> could not be found!"),
    FAIL_SELF("cannotGiveToSelf", "<red>You can't give a pet to yourself!"),
    FAIL_NOT_OWNED("notYourPet", "<red>That's not your pet!"),
    FAIL_PLAYER_LEFT("playerLeft", "<red>The player you were trying to give that pet to has since left the server."),
    CANCEL_SUCCESS("cancelSuccess", "<green>Cancelled transferring a pet!"),
    CANCEL_FAIL("cancelFail", "<red>You haven't tried to transfer a pet!")
    ;

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
