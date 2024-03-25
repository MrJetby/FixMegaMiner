package me.jetby.megaminer.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PS {

    public static String ps(Player p, String string) {
        return PlaceholderAPI.setPlaceholders(p, string
                .replace("%player%", p.getName())
                .replace('&', '§'));
    }
}