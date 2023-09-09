package me.lucyydotp.givepet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class GivePet extends JavaPlugin {

    private final TransferManager transferManager = new TransferManager(true);

    public TransferManager transferManager() {
        return transferManager;
    }

    @Override
    public void onEnable() {
        FileConfiguration cfg = getConfig();
        cfg.options().copyDefaults(true);

        // todo: hook these up to Message
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

        getCommand("givepet").setExecutor(new GivePetCommand(transferManager));

        getServer().getPluginManager().registerEvents(new InteractListener(this), this);

    }
}
