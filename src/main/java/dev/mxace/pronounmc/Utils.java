package dev.mxace.pronounmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static String formatMessage(String text, boolean ok) {
        ChatColor okColor = ok ? ChatColor.GREEN : ChatColor.RED;
        return ChatColor.BOLD.toString() + okColor + "[PronounMC] " + ChatColor.RESET + okColor + text;
    }

    public static boolean requirePermission(CommandSender commandSender, Permission permission) {
        if (!commandSender.hasPermission(permission)) {
            commandSender.sendMessage(formatMessage("Missing the '" + permission.getName() + "' permission.", false));
            return true;
        }

        return false;
    }

    public static class Config {
        public final File file;
        public final YamlConfiguration yaml;

        public Config(File file, YamlConfiguration yaml) {
            this.file = file;
            this.yaml = yaml;
        }

        public void save() {
            try {
                yaml.save(file);
            } catch (IOException ex) {
                Bukkit.getLogger().severe("Could not save config to '" + file.getPath() + "'.");
            }
        }
    }

    public static Config getConfig(String filepath) {
        File file = new File(PronounMC.getInstance().getDataFolder(), filepath);

        // Create the file if non-existent
        if (!file.exists()) {
            PronounMC.getInstance().saveResource(filepath, false);
        }

        YamlConfiguration yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().severe("Could not load config from '" + file.getPath() + "'.");
        }

        return new Config(file, yaml);
    }
}
