package dev.mxace.pronounmc.commands.executors;

import dev.mxace.pronounmc.Utils;
import dev.mxace.pronounmc.manager.PronounsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ClearpronounsCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        Player p = (Player) commandSender;
        if (p == null) {
            // Only players can send this command.
            return false;
        }

        return switch (args.length) {
            case 0 -> { // /clearpronouns
                if (Utils.requirePermission(p, new Permission("pronounmc.clear.self"))) yield true;

                PronounsManager.clearPronouns(p);
                p.sendMessage(Utils.formatMessage("Your pronouns preference has been cleared.", true));

                yield true;
            }

            case 1 -> { // /clearpronouns [player]
                if (Utils.requirePermission(p, new Permission("pronounmc.clear.other"))) yield true;

                Player p2 = Bukkit.getServer().getPlayer(args[0]);
                if (p2 == null) {
                    p.sendMessage(Utils.formatMessage(args[0] + " is currently offline.", false));
                    yield true;
                }

                PronounsManager.clearPronouns(p2);
                p.sendMessage(Utils.formatMessage(args[0] + "'s preferred pronouns were cleared.", true));

                yield true;
            }

            default -> false;
        };
    }
}
