package dev.mxace.pronounmc.commands.tabcompleters;

import dev.mxace.pronounmc.manager.PronounsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class SetpronounsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length == 1) return PronounsManager.getPronounsList();
        return null;
    }
}
