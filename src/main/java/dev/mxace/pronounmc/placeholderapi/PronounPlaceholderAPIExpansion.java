package dev.mxace.pronounmc.placeholderapi;

import dev.mxace.pronounmc.manager.PronounsManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PronounPlaceholderAPIExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "_Mx_Ace";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pronounmc";
    }

    @Override
    public @NotNull String getVersion() {
        return "2025.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        PronounsManager.Pronouns pronouns = PronounsManager.getPronouns(player.getPlayer());

        if (params.equalsIgnoreCase("full")) return pronouns.getString();
        if (params.equalsIgnoreCase("identifier")) return pronouns.getIdentifier();
        if (params.equalsIgnoreCase("note")) return pronouns.getNote();

        return null;
    }
}
