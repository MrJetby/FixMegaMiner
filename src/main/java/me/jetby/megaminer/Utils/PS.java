package me.jetby.megaminer.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PS {
    private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static String color(String text) {
        if (text == null) {
            return "Error processing the message";
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String colorCode = text.substring(matcher.start() + 1, matcher.end());
            text = text.replace(matcher.group(), String.valueOf(ChatColor.of((String)colorCode)));
            matcher = pattern.matcher(text);
        }
        return ChatColor.translateAlternateColorCodes((char)'&', (String)text);
    }
    public static String ps(Player p, String string) {
        return PlaceholderAPI.setPlaceholders(p, color(string)
                .replace("%player%", p.getName())
                );
    }
}