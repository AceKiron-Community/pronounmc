package dev.mxace.pronounmc.commands.executors;

import dev.mxace.pronounmc.Utils;
import dev.mxace.pronounmc.manager.PronounsManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class SetpronounsCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        Player p = (Player) commandSender;
        if (p == null) {
            // Only players can send this command.
            return false;
        }

        PronounsManager.Pronouns pronouns = PronounsManager.getPronouns(args[0]);
        if (pronouns == null) {
            // Invalid pronouns provided.
            return false;
        }

        return switch (args.length) {
            case 1 -> { // /setpronouns <pronouns>
                if (Utils.requirePermission(p, new Permission("pronounmc.set.self"))) yield true;

                PronounsManager.setPronouns(p, pronouns);
                p.sendMessage(Utils.formatMessage("Changed your preferred pronouns to '" + args[0] + "'.", true));

                yield true;
            }

            case 2 -> { // /setpronouns <pronouns> [player]
                if (Utils.requirePermission(p, new Permission("pronounmc.set.other"))) yield true;

                Player p2 = Bukkit.getServer().getPlayer(args[1]);
                if (p2 == null) {
                    p.sendMessage(Utils.formatMessage(args[1] + " is currently offline.", false));
                    yield true;
                }

                PronounsManager.setPronouns(p2, pronouns);
                p.sendMessage(Utils.formatMessage("Changed " + args[1] + "'s preferred pronouns to '" + args[0] + "'.", true));

                yield true;
            }

            default -> false;
        };
    }
}
