package me.lucyy.givepet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GivePetCommand implements CommandExecutor, TabCompleter {
	@NonNull private final GivePet plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player");
			return true;
		}
		if (args.length != 1) return false;

		Player transferSender = (Player) sender;

		if (args[0].equals("cancel")) {
			// try to remove any outstanding attempts
			if (plugin.cancelTransfer(transferSender.getUniqueId())) {
				sender.sendMessage(plugin.getMsg("cancelSuccess"));
			} else sender.sendMessage(plugin.getMsg("cancelFail"));
			return true;
		}

		// start a new transfer
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(plugin.getMsg("playerNotFound"));
			return true;
		}
		if (sender.equals(target)) {
			sender.sendMessage(plugin.getMsg("selfGive"));
			return true;
		}

		plugin.cancelTransfer(transferSender.getUniqueId());

		// create new transfer and send message
		sender.sendMessage(plugin.getMsg("rightClickPrompt"));
		plugin.getTransferAttempts().add(new TransferAttempt(transferSender.getUniqueId(), target.getUniqueId()));
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
