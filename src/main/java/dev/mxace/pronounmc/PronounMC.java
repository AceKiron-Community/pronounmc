package dev.mxace.pronounmc;

import dev.mxace.pronounmc.commands.executors.ClearpronounsCommandExecutor;
import dev.mxace.pronounmc.commands.executors.PronounsnoteCommandExecutor;
import dev.mxace.pronounmc.commands.executors.ReloadpronounmcCommandExecutor;
import dev.mxace.pronounmc.commands.executors.SetpronounsCommandExecutor;
import dev.mxace.pronounmc.commands.tabcompleters.PronounsnoteTabCompleter;
import dev.mxace.pronounmc.commands.tabcompleters.SetpronounsTabCompleter;
import dev.mxace.pronounmc.manager.DatabaseManager;
import dev.mxace.pronounmc.manager.PronounsManager;
import dev.mxace.pronounmc.placeholderapi.PronounPlaceholderAPIExpansion;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PronounMC extends JavaPlugin {
    private static PronounMC instance;
    public static PronounMC getInstance() {
        return instance;
    }

    private PronounPlaceholderAPIExpansion pronounPlaceholderAPIExpansion;

    @Override
    public void onEnable() {
        instance = this;

        enable();
    }

    private void enable() {
        // Create ~/plugins/PronounMC folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Register Placeholder API expansion
        Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPI != null) {
            Bukkit.getLogger().info("PlaceholderAPI found!");
            pronounPlaceholderAPIExpansion = new PronounPlaceholderAPIExpansion();
            pronounPlaceholderAPIExpansion.register();
        } else {
            Bukkit.getLogger().warning("PlaceholderAPI not found!");
        }

        // Register commands
        getCommand("clearpronouns").setExecutor(new ClearpronounsCommandExecutor());

        getCommand("pronounsnote").setExecutor(new PronounsnoteCommandExecutor());
        getCommand("pronounsnote").setTabCompleter(new PronounsnoteTabCompleter());

        getCommand("reloadpronounmc").setExecutor(new ReloadpronounmcCommandExecutor());

        getCommand("setpronouns").setExecutor(new SetpronounsCommandExecutor());
        getCommand("setpronouns").setTabCompleter(new SetpronounsTabCompleter());

        // Reload managers
        DatabaseManager.reload();
        PronounsManager.reload();
    }

    private void disable() {
        if (pronounPlaceholderAPIExpansion != null) {
            pronounPlaceholderAPIExpansion.unregister();
        }
    }

    public void reload(CommandSender commandSender) {
        disable();
        enable();

        commandSender.sendMessage(Utils.formatMessage("Plugin reloaded succesfully!", true));
    }

    @Override
    public void onDisable() {
        disable();

        instance = null;
    }
}
