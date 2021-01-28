package me.lucyy.givepet;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class TransferAttempt {
	@NonNull UUID giver;
	@NonNull UUID receiver;
}
