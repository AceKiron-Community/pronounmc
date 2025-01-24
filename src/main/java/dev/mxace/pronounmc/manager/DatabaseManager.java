package dev.mxace.pronounmc.manager;

import dev.mxace.pronounmc.PronounMC;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static Connection connection;

    public static void reload() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + PronounMC.getInstance().getDataFolder().getAbsolutePath() + "/database.sqlite");

            executeQuery("CREATE TABLE IF NOT EXISTS pronouns_table (player_uuid TEXT UNIQUE, pronouns TEXT);");
        } catch (SQLException ex) {
            Bukkit.getLogger().severe("SQLException: " + ex.getSQLState() + "; Message: " + ex.getMessage());
        }
    }

    public static void executeQuery(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    public static Map<String, String> getSingleResultSet(String query, List<String> columns, List<String> defaultValues) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            rs.next();

            Map<String, String> result = new HashMap<>();

            for (String column : columns) {
                result.put(column, rs.getString(column));
            }

            return result;
        }
    }
}
