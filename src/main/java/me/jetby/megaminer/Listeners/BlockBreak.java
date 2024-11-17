package me.jetby.megaminer.Listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

import static me.jetby.megaminer.MegaMiner.*;
import static me.jetby.megaminer.Utils.Actions.*;


public class BlockBreak implements Listener {

    private ConfigurationSection blocks = cfg.getConfigurationSection("Blocks");
    private List<String> DisabledWorlds;
    private List<String> DisabledEnchantments;


    @EventHandler
    public void OnBlockBreak(BlockBreakEvent e) {

        Block block = e.getBlock();
        String blockName = block.getType().name();

        Player p = e.getPlayer();

        DisabledWorlds = blocks.getStringList(blockName + ".Disabled-Worlds");
        DisabledEnchantments = blocks.getStringList(blockName + ".Disabled-Enchantments");


        World world = p.getWorld();

            if (blocks.contains(blockName)) {
                if (!(DisabledWorlds.contains(world.getName()))) {
                        ItemStack itemInHand = p.getInventory().getItemInMainHand();
                        if (itemInHand==null) {return;}
                        ItemMeta itemMeta = itemInHand.getItemMeta();

                        if (itemMeta != null && itemMeta.hasEnchants()) {
                            for (Enchantment enchantment : itemMeta.getEnchants().keySet()) {
                                // есть ли запрещенное зачарование на предмете в руках
                                if (DisabledEnchantments.contains(enchantment.getKey().getKey())) {
                                    return;
                                }
                            }
                        }

                    if (activeTimers.containsKey(p)) {
                        cancelTimer(e.getPlayer());
                    }
                    int Chance = cfg.getInt("Blocks." + blockName + ".Chance", -1);

                    if (new Random().nextInt(100) > Chance) {
                        // Событие не сработало
                        return;
                    }

                    List<String> actions = blocks.getStringList(blockName + ".Actions");
                    for (String action : actions) {
                        Actions(p, action);
                    }

                }
            }
    }private static void cancelTimer(Player player) {
        if (activeTimers.containsKey(player)) {
            activeTimers.get(player).cancel();
            activeTimers.remove(player);

            if (playerBossBars.containsKey(player)) {
                playerBossBars.get(player).removeAll();
                playerBossBars.remove(player);
            }

            playerCountdowns.remove(player);

            // Получаем действия из конфига и выполняем их
            List<String> actions = cfg.getStringList("actions.cancel");
            for (String action : actions) {
                Actions(player, action);
            }

        }
    }
}
