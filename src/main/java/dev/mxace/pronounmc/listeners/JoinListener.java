package dev.mxace.pronounmc.listeners;

import dev.mxace.pronounmc.manager.DatabaseManager;
import dev.mxace.pronounmc.manager.PronounsManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JoinListener implements Listener {
    private List<String> keys = new ArrayList<>();
    private List<String> values = new ArrayList<>();

    public JoinListener() {
        keys.add("pronouns");
        values.add(PronounsManager.getDefault());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            Map<String, String> result = DatabaseManager.getSingleResultSet("SELECT pronouns FROM pronouns_table WHERE player_uuid = '" + event.getPlayer().getUniqueId() + "';", keys, values);
            PronounsManager.handleJoinEvent(event.getPlayer(), result.get("pronouns"));
        } catch (SQLException ex) {
            Bukkit.getLogger().severe("SQLException: " + ex.getSQLState() + "; Message: " + ex.getMessage());
        }
    }
}
