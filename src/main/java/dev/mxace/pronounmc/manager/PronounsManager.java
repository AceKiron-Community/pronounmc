package dev.mxace.pronounmc.manager;

import dev.mxace.pronounmc.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PronounsManager {
    public static class Pronouns {
        private static final Map<String, Pronouns> stringPronounsMap = new HashMap<>();
        private static final Map<Player, Pronouns> playerPronounsMap = new HashMap<>();
        private static Pronouns defaultPronouns;

        private final String identifier;
        private final String subject, object, possessiveDeterminer, possessivePronoun, reflexive;
        private final String note;

        private Pronouns(String identifier, ConfigurationSection configurationSection) {
            this.identifier = identifier;

            subject = configurationSection.getString("subject");
            object = configurationSection.getString("object");
            possessiveDeterminer = configurationSection.getString("possessiveDeterminer");
            possessivePronoun = configurationSection.getString("possessivePronoun");
            reflexive = configurationSection.getString("reflexive");
            note = configurationSection.getString("note");
        }

        public String getIdentifier() {
            return identifier;
        }

        public String getSubject() {
            return subject;
        }

        public String getObject() {
            return object;
        }

        public String getPossessiveDeterminer() {
            return possessiveDeterminer;
        }

        public String getPossessivePronoun() {
            return possessivePronoun;
        }

        public String getReflexive() {
            return reflexive;
        }

        public String getNote() {
            return note;
        }

        public String getString() {
            return getSubject() + "/" + getObject() + "/" + getPossessiveDeterminer() + "/" + getPossessivePronoun() + "/" + getReflexive();
        }

        public static void reload() {
            stringPronounsMap.clear();
            playerPronounsMap.clear();

            Utils.Config config = Utils.getConfig("pronouns.yml");

            ConfigurationSection pronounsConfigSection = config.yaml.getConfigurationSection("pronouns");

            for (String identifier : pronounsConfigSection.getKeys(false)) {
                stringPronounsMap.put(identifier, new Pronouns(identifier, Objects.requireNonNull(pronounsConfigSection.getConfigurationSection(identifier))));
            }

            defaultPronouns = stringPronounsMap.get(config.yaml.getString("defaultPronouns"));

            if (defaultPronouns == null) {
                Bukkit.getLogger().severe("Default pronouns aren't available!");
            }

            try {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    ResultSet rs = DatabaseManager.getSingleResultSet("SELECT pronouns FROM pronouns_table WHERE player_uuid = '" + p.getUniqueId() + "';");
                    if (!rs.isClosed()) {
                        String identifier = rs.getString("pronouns");
                        playerPronounsMap.put(p, stringPronounsMap.get(identifier));
                    }
                }
            } catch (SQLException ex) {
                Bukkit.getLogger().severe("SQLException: " + ex.getSQLState() + "; Message: " + ex.getMessage());
            }
        }
    }

    public static void reload() {
        Pronouns.reload();
    }

    public static Pronouns getPronouns(String identifier) {
        return Pronouns.stringPronounsMap.get(identifier);
    }

    public static Pronouns getPronouns(Player p) {
        if (hasPronouns(p)) return Pronouns.playerPronounsMap.get(p);
        return Pronouns.defaultPronouns;
    }

    public static boolean hasPronouns(Player p) {
        return Pronouns.playerPronounsMap.containsKey(p);
    }

    public static void setPronouns(Player p, Pronouns p2) {
        Pronouns.playerPronounsMap.put(p, p2);
        try {
            DatabaseManager.executeQuery("REPLACE INTO pronouns_table (pronouns) VALUES ('" + p2.identifier + "') WHERE player_uuid = '" + p.getUniqueId() + "';");
        } catch (SQLException ex) {
            Bukkit.getLogger().severe("SQLException: " + ex.getSQLState() + "; Message: " + ex.getMessage());
        }
    }

    public static void clearPronouns(Player p) {
        Pronouns.playerPronounsMap.remove(p);
        try {
            DatabaseManager.executeQuery("DELETE FROM pronouns_table WHERE player_uuid = '" + p.getUniqueId() + "';");
        } catch (SQLException ex) {
            Bukkit.getLogger().severe("SQLException: " + ex.getSQLState() + "; Message: " + ex.getMessage());
        }
    }

    public static List<String> getPronounsList() {
        return Pronouns.stringPronounsMap.keySet().stream().toList();
    }
}
