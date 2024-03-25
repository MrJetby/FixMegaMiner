package me.jetby.megaminer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.jetby.megaminer.MegaMiner.instance;
import static me.jetby.megaminer.MegaMiner.settings;
import static me.jetby.megaminer.Utils.PS.ps;

public class cmds implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player) sender;

        if (args[0].equalsIgnoreCase("reload")) {
            if (!(p.hasPermission("megaminer.admin"))) {
                p.sendMessage(ps(p, settings.getString("messages.noperm")));
                return true;
            }
            instance.settingsLoad();
            sender.sendMessage(ps(p, settings.getString("messages.reload")));
            return true;
        }

        return true;
    }
}
