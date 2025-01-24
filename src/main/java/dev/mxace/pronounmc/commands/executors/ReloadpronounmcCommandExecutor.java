package dev.mxace.pronounmc.commands.executors;

import dev.mxace.pronounmc.PronounMC;
import dev.mxace.pronounmc.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class ReloadpronounmcCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (Utils.requirePermission(commandSender, new Permission("pronounmc.reload"))) return true;

        PronounMC.getInstance().reload(commandSender);

        return true;
    }
}
