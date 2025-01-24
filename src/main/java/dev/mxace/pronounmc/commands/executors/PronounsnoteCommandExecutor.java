package dev.mxace.pronounmc.commands.executors;

import dev.mxace.pronounmc.Utils;
import dev.mxace.pronounmc.manager.PronounsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PronounsnoteCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length == 0) return false;

        PronounsManager.Pronouns pronouns = PronounsManager.getPronouns(args[0]);
        if (pronouns == null) {
            // Invalid pronouns provided.
            return false;
        }

        String note = pronouns.getNote();
        if (note == null) {
            commandSender.sendMessage(Utils.formatMessage("This set of pronouns does not have a note attached.", false));
            return true;
        }

        commandSender.sendMessage(Utils.formatMessage(pronouns.getNote(), true));
        return true;
    }
}
