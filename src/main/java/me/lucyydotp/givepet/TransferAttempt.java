package me.lucyydotp.givepet;

import java.util.UUID;

/**
 * An attempt to transfer a pet to another player.
 *
 * @param pet      the pet itself
 * @param giver    the player giving the pet
 * @param receiver the player receiving the pet
 */
public record TransferAttempt(UUID pet, UUID giver, UUID receiver) {

}
