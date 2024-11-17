package me.jetby.megaminer;

import me.jetby.megaminer.Commands.Command;
import me.jetby.megaminer.Listeners.BlockBreak;
import me.jetby.megaminer.Utils.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public final class MegaMiner extends JavaPlugin {
    private static MegaMiner instance;

    public static MegaMiner getInstance() {
        return instance;
    }

    private static Economy econ = null;
    public static YamlConfiguration cfg;
    @Override
    public void onEnable() {
        cfgReload();
        int pluginId = 20330;
        Metrics metrics = new Metrics(this, pluginId);

        instance = this;

        getCommand("MegaMiner").setExecutor(new Command());
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);


    }


    public void cfgReload() {
        File file = new File(getDataFolder().getAbsolutePath() + "/config.yml");
        if (file.exists()) {
            getLogger().log(Level.INFO, "Config loaded successfully!. (config.yml)");
            cfg = YamlConfiguration.loadConfiguration(file);
        } else {
            saveResource("config.yml", false);
            cfg = YamlConfiguration.loadConfiguration(file);
        }
    }

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

}
