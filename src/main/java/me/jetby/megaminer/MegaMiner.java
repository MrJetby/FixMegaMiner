package me.jetby.megaminer;

import me.jetby.megaminer.Commands.cmds;
import me.jetby.megaminer.Listeners.BlockBreak;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class MegaMiner extends JavaPlugin {
    public static MegaMiner instance;

    public static Economy eco;
    public static YamlConfiguration settings;
    @Override
    public void onEnable() {
        instance = this;

        getCommand("MegaMiner").setExecutor(new cmds());
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);

        settingsLoad();
        settingsSave();

        setupEconomy();
    }

    public void settingsLoad() {
        saveResource("settings.yml", false);
        File file = new File(getDataFolder().getAbsolutePath() + "/settings.yml");
        settings = YamlConfiguration.loadConfiguration(file);
    }

    public void settingsSave() {
        try {
            File file = new File(getDataFolder(), "settings.yml");
            settings.save(file);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Не удалось сохранить файл db.yml", e);
        }
    }
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        eco = rsp.getProvider();
        return eco != null;
    }
}
