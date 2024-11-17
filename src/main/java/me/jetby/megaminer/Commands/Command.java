package me.jetby.megaminer.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import static me.jetby.megaminer.MegaMiner.*;
import static me.jetby.megaminer.Utils.PS.ps;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length==0) {
                sender.sendMessage("/megaminer reload");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!(p.hasPermission("megaminer.admin"))) {
                    p.sendMessage(ps(p, cfg.getString("messages.noperm", "You don't have permission!")));
                    return true;
                }
                getInstance().cfgReload();
                sender.sendMessage(ps(p, cfg.getString("messages.reload", "Config reloaded!")));
                return true;
            }
        } else {
            if (args.length==0) {
                sender.sendMessage("/megaminer reload");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                getInstance().cfgReload();
                sender.sendMessage("Config reloaded!");
                return true;
            }
        }



        return false;
    }
}
