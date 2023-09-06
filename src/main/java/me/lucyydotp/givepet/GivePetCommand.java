package me.lucyydotp.givepet;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GivePetCommand implements CommandExecutor, TabCompleter {
    private final GivePet plugin;

    public GivePetCommand(GivePet plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player");
            return true;
        }
        if (args.length != 1) return false;

        if (args[0].equals("cancel")) {
            // try to remove any outstanding attempts
            if (plugin.cancelTransfer(player.getUniqueId())) {
                sender.sendMessage(Message.CANCEL_SUCCESS.text());
            } else sender.sendMessage(Message.CANCEL_FAIL.text());
            return true;
        }

        // start a new transfer
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {

            sender.sendMessage(Message.FAIL_PLAYER_NOT_FOUND.text(Placeholder.unparsed("player", args[0])));
            return true;
        }
        if (sender.equals(target)) {
            sender.sendMessage(Message.FAIL_SELF.text());
            return true;
        }

        plugin.cancelTransfer(player.getUniqueId());

        // create new transfer and send message
        sender.sendMessage(Message.START.text());
        plugin.transferAttempts().put(player.getUniqueId(),
                new TransferAttempt(null,
                        player.getUniqueId(),
                        target.getUniqueId()
                )
        );
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) return null;
        List<String> outList = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(x -> {
            if (!x.equals(sender)) outList.add(x.getName());
        });
        outList.add("cancel");
        return outList;
    }
}
