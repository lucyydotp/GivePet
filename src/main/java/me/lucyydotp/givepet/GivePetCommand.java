package me.lucyydotp.givepet;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GivePetCommand implements TabExecutor {
    private final TransferManager transferManager;

    public GivePetCommand(TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    private boolean cancel(Player player) {
        transferManager.cancelTransfer(player);
        return true;
    }

    private boolean accept(Player player, String[] args) {
        if (args.length != 2) return false;
        final var sender = Bukkit.getPlayerExact(args[1]);
        if (sender == null) {
            player.sendMessage(Message.FAIL_PLAYER_NOT_FOUND.text(Placeholder.unparsed("player", args[1])));
            return true;
        }
        transferManager.acceptTransfer(sender, player);
        return true;
    }

    private boolean deny(Player player, String[] args) {
        if (args.length != 2) return false;
        final var sender = Bukkit.getPlayerExact(args[1]);
        if (sender == null) {
            player.sendMessage(Message.FAIL_PLAYER_NOT_FOUND.text(Placeholder.unparsed("player", args[1])));
            return true;
        }
        transferManager.denyTransfer(sender, player);
        return true;
    }

    private boolean newTransfer(Player player, String[] args) {
        if (args.length != 1) return false;
        final var recipient = Bukkit.getPlayerExact(args[0]);
        if (recipient == null) {
            player.sendMessage(Message.FAIL_PLAYER_NOT_FOUND.text(Placeholder.unparsed("player", args[0])));
            return true;
        }
        transferManager.startTransfer(player, recipient);
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be ran by players.");
            return true;
        }

        return switch (args[0]) {
            case "cancel" -> cancel(player);
            case "accept" -> accept(player, args);
            case "deny" -> deny(player, args);
            default -> newTransfer(player, args);
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
