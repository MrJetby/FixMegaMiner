package me.jetby.megaminer.Utils;

import me.jetby.megaminer.MegaMiner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static me.jetby.megaminer.MegaMiner.*;
import static me.jetby.megaminer.Utils.PS.ps;
import static org.bukkit.Bukkit.getLogger;

public class Actions {

    private static int sum = 0;
    public static final Random RANDOM = new Random();
    public static Map<Player, BossBar> playerBossBars = new HashMap<>();
    public static Map<Player, BukkitRunnable> activeTimers = new HashMap<>();
    public static Map<Player, Integer> playerCountdowns = new HashMap<>();

    public static void Actions(Player player, String command) {
        String[] args = command.split(" ");
        String withoutCMD = command.replace(args[0] + " ", "");


        switch (args[0]) {
            case "[MESSAGE]": {
                player.sendMessage(ps(player, withoutCMD.replace("%money%", String.valueOf(sum))));
                break;
            }
            case "[GIVEMONEY]": {

                if (!getInstance().setupEconomy() ) {
                    getLogger().severe("For the [GIVEMONEY] feature to work you need the Vault plugin, but I couldn't find it!");
                    return;
                }
                int Min = 0;
                int Max = 0;
                try {
                    String[] message = ps(player, withoutCMD).split("-");
                    if (message.length >= 1) {
                        Min = Integer.parseInt(message[0].trim());
                        if (message.length >= 2) {
                            Max = Integer.parseInt(message[1].trim());
                        }
                    }

                    if (Min > Max) {
                        System.out.println("Ошибка: Минимальное значение (Min) не может быть больше максимального (Max).");
                        break;
                    }
                    if (Min < 0 || Max < 0) {
                        System.out.println("Ошибка: Значения Min и Max должны быть положительными.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: Некорректный формат чисел для Min и Max.");
                    break;
                }
                sum = RANDOM.nextInt(Max - Min + 1) + Min;

                EconomyResponse r = getEconomy().depositPlayer(player, sum);
                if (!r.transactionSuccess()) {
                    player.sendMessage(String.format("§cПроизошла ошибка: %s", r.errorMessage));
                }
                break;
            }
            case "[PLAYER]": {
                Bukkit.dispatchCommand(player, ps(player, withoutCMD
                        .replace("%player%", player.getName())
                        .replace("%money%", String.valueOf(sum))
                ));
                break;
            }
            case "[CONSOLE]": {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ps(player, withoutCMD
                        .replace("%player%", player.getName())
                        .replace("%money%", String.valueOf(sum))
                ));
                break;
            }
            case "[ACTIONBAR]": {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ps(player, withoutCMD
                        .replace("%player%", player.getName())
                        .replace("%money%", String.valueOf(sum))
                )));
                break;
            }
//             case "[BOSSBAR]": {
//                String title = ps(player, withoutCMD
//                        .replace("%money%", String.valueOf(sum))
//                );
//                BarColor barColor = BarColor.RED;
//                BarStyle barStyle = BarStyle.SOLID;
//                int time = 3;
//
//                for (String arg : args) {
//                    if (arg.startsWith("-color:")) {
//                        try {
//                            barColor = BarColor.valueOf(arg.replace("-color:", "").toUpperCase());
//                        } catch (IllegalArgumentException e) {
//                            return;
//                        }
//                        continue;
//                    }
//                    if (arg.startsWith("-style:")) {
//                        try {
//                            barStyle = BarStyle.valueOf(arg.replace("-style:", "").toUpperCase());
//                        } catch (IllegalArgumentException e) {
//                            continue;
//                        }
//                    } if (arg.startsWith("-duration:")) {
//                        try {
//                            time = Integer.parseInt(arg.replace("-duration:", "").toUpperCase());
//                        } catch (IllegalArgumentException e) {
//                            continue;
//                        }
//                    }
//                }
//
//                BossBar bossBar = Bukkit.createBossBar(title, barColor, barStyle);
//
//                bossBar.addPlayer(player);
//                playerBossBars.put(player, bossBar);
//                playerCountdowns.put(player, time);
//
//                BukkitRunnable task = new BukkitRunnable() {
//                    int currentCountdown = playerCountdowns.get(player);
//                    @Override
//                    public void run() {
//                        if (currentCountdown > 0) {
//                            playerCountdowns.put(player, currentCountdown - 1);
//                        } else {
//                            bossBar.removeAll();
//                            activeTimers.remove(player);
//                            playerBossBars.remove(player);
//                            playerCountdowns.remove(player);
//                            cancel();
//                        }
//                    }
//                };task.runTaskTimer(getInstance(), 0, 20);
//                break;
//            }

            case "[SOUND]": {
                float volume = 1.0f;
                float pitch = 1.0f;
                for (String arg : args) {
                    if (arg.startsWith("-volume:")) {
                        volume = Float.parseFloat(arg.replace("-volume:", ""));
                        continue;
                    }
                    if (!arg.startsWith("-pitch:")) continue;
                    pitch = Float.parseFloat(arg.replace("-pitch:", ""));
                }
                player.playSound(player.getLocation(), Sound.valueOf((String) args[1]), volume, pitch);
                break;
            }
            case "[EFFECT]": {
                int strength = 0;
                int duration = 1;
                for (String arg : args) {
                    if (arg.startsWith("-strength:")) {
                        strength = Integer.parseInt(arg.replace("-strength:", ""));
                        continue;
                    }
                    if (!arg.startsWith("-duration:")) continue;
                    duration = Integer.parseInt(arg.replace("-duration:", ""));
                }
                PotionEffectType effectType = PotionEffectType.getByName((String) args[1]);
                if (effectType == null) {
                    return;
                }
                if (player.hasPotionEffect(effectType)) {
                    return;
                }
                player.addPotionEffect(new PotionEffect(effectType, duration * 20, strength));
                break;
            }
            case "[TITLE]": {
                String title = "";
                String subTitle = "";
                int fadeIn = 1;
                int stay = 3;
                int fadeOut = 1;
                for (String arg : args) {
                    if (arg.startsWith("-fadeIn:")) {
                        fadeIn = Integer.parseInt(arg.replace("-fadeIn:", ""));
                        withoutCMD = withoutCMD.replace(arg, "");
                        continue;
                    }
                    if (arg.startsWith("-stay:")) {
                        stay = Integer.parseInt(arg.replace("-stay:", ""));
                        withoutCMD = withoutCMD.replace(arg, "");
                        continue;
                    }
                    if (!arg.startsWith("-fadeOut:")) continue;
                    fadeOut = Integer.parseInt(arg.replace("-fadeOut:", ""));
                    withoutCMD = withoutCMD.replace(arg, "");
                }
                String[] message = ps(player, withoutCMD).split(";");
                if (message.length >= 1) {
                    title = message[0].replace("%money%", String.valueOf(sum));
                    if (message.length >= 2) {
                        subTitle = message[1].replace("%money%", String.valueOf(sum));
                    }
                }
                player.sendTitle(title, subTitle, fadeIn * 20, stay * 20, fadeOut * 20);
            }
        }
    }

}
