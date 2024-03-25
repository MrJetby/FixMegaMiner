package me.jetby.megaminer.Listeners;

import com.sun.org.apache.xerces.internal.xs.StringList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static me.jetby.megaminer.MegaMiner.*;
import static me.jetby.megaminer.Utils.PS.ps;

public class BlockBreak implements Listener {

    private ConfigurationSection blockscfg;

    @EventHandler
    public void OnBlockBreak(BlockBreakEvent e) {

        blockscfg = settings.getConfigurationSection("Blocks");
        Block block = e.getBlock();
        String blockName = block.getType().name();
        Player p = e.getPlayer();

        int Min = blockscfg.getInt(blockName + ".Money.Min");
        int Max = blockscfg.getInt(blockName + ".Money.Max");
        int Chance = blockscfg.getInt(blockName + ".Chance");
        List<String> commands = blockscfg.getStringList(blockName + ".Commands");
        List<String> message = blockscfg.getStringList(blockName + ".Messages.Message");
        List<String> disabledworlds = blockscfg.getStringList(blockName + ".Disabled-Worlds");
        List<String> disabledenchantments = blockscfg.getStringList(blockName + ".Disabled-Enchantments");
        String actionbar = blockscfg.getString(blockName + ".Messages.Actionbar");

        World world = p.getWorld();

            if (blockscfg.contains(blockName)) {
                if (!(disabledworlds.contains(world.getName()))) {
                        ItemStack itemInHand = p.getInventory().getItemInMainHand();
                        ItemMeta itemMeta = itemInHand.getItemMeta();
                        if (itemMeta != null && itemMeta.hasEnchants()) {
                            for (Enchantment enchantment : itemMeta.getEnchants().keySet()) {
                                // Проверяем, есть ли запрещенное зачарование на предмете в руках
                                if (disabledenchantments.contains(enchantment.getKey().getKey())) {
                                    return; // Просто завершаем метод, не делая ничего
                                }
                            }
                        }

                    if (Math.random() * 100 < Chance) {

                        int sum = new Random().nextInt(Max - Min + 1) + Min;
                        eco.depositPlayer(p, sum);

                            if (!(commands.isEmpty())) {
                                for (String cmds : commands) {
                                    String newcmds = cmds
                                            .replace("%player%", p.getName())
                                            .replace("%money%", String.valueOf(sum));
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ps(p, newcmds));
                                }
                            }
                            if (!(message.isEmpty())) {
                                for (String messages : message) {
                                    String newmessages = messages
                                            .replace("%money%", String.valueOf(sum));
                                    p.sendMessage(ps(p, newmessages));
                                }
                            }

                            if (!(actionbar.isEmpty())) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                        ps(p, actionbar
                                                .replace("%money%", String.valueOf(sum)))));
                            }
                    }
                }
            }
    }
}
