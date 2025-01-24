package dev.mxace.pronounmc.manager;

import dev.mxace.pronounmc.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PronounsManager {
    private static final List<String> keys = new ArrayList<>();
    private static final List<String> values = new ArrayList<>();

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
        }
    }

    public static void reload() {
        keys.clear();
        values.clear();

        keys.add("pronouns");

        Pronouns.reload();

        values.add(Pronouns.defaultPronouns.identifier);

        try {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                Map<String, String> result = DatabaseManager.getSingleResultSet("SELECT pronouns FROM pronouns_table WHERE player_uuid = '" + p.getUniqueId() + "';", keys, values);
                Pronouns.playerPronounsMap.put(p, Pronouns.stringPronounsMap.get(result.get("pronouns")));
            }
        } catch (SQLException ex) {
            Bukkit.getLogger().severe("SQLException: " + ex.getSQLState() + "; Message: " + ex.getMessage());
        }
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
            DatabaseManager.executeQuery("DELETE FROM pronouns_table WHERE player_uuid = '" + p.getUniqueId() + "';");
            DatabaseManager.executeQuery("INSERT INTO pronouns_table (player_uuid, pronouns) VALUES ('" + p.getUniqueId() + "', '" + p2.identifier + "');");
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

    public static void handleJoinEvent(Player p, String identifier) {
        Pronouns.playerPronounsMap.put(p, Pronouns.stringPronounsMap.get(identifier));
    }

    public static String getDefault() {
        return Pronouns.defaultPronouns.identifier;
    }
}
