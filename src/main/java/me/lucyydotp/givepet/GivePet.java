package me.lucyydotp.givepet;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GivePet extends JavaPlugin {

    private final Map<UUID, TransferAttempt> transferAttempts = new HashMap<>();

    public Map<UUID, TransferAttempt> transferAttempts() {
        return transferAttempts;
    }

    public boolean cancelTransfer(UUID uuid) {
        return transferAttempts.remove(uuid) != null;
    }

    /**
     * @deprecated use minimessage instead
     */
    @Deprecated
    public String getMsg(String key) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(key));
    }

    @Override
    public void onEnable() {
        FileConfiguration cfg = getConfig();
        cfg.options().copyDefaults(true);
        cfg.addDefault("rightClickPrompt", "<yellow>Please right click the pet you would like to give");
        cfg.addDefault("playerNotFound", "&cThat player could not be found!");
        cfg.addDefault("cancelFail", "&cYou haven't tried to transfer a pet!");
        cfg.addDefault("cancelSuccess", "&aCancelled transferring a pet!");
        cfg.addDefault("playerLeft", "&cThe player you were trying to give that pet to has since left the server.");
        cfg.addDefault("sentReceiverMsg", "{sender}&a gave you a pet &f{type}&a!");
        cfg.addDefault("sentSenderMsg", "&aYou gave your pet successfully!");
        cfg.addDefault("notOwned", "&cThat's not your pet!");
        cfg.addDefault("selfGive", "&cYou can't give a pet to yourself!");
        saveConfig();

        GivePetCommand cmd = new GivePetCommand(this);

        getCommand("givepet").setExecutor(cmd);
        getCommand("givepet").setTabCompleter(cmd);

        getServer().getPluginManager().registerEvents(new InteractListener(this), this);

    }
}
