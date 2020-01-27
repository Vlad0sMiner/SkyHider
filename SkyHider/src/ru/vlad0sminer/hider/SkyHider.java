package ru.vlad0sminer.hider;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vlad0sminer.hider.commands.CommandHalfHide;
import ru.vlad0sminer.hider.commands.CommandHideAll;

import java.io.File;

public class SkyHider extends JavaPlugin implements Listener {

    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(new CommandHideAll(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandHalfHide(this), this);
        getCommand("hideall").setExecutor(new CommandHideAll(this));
        getCommand("halfhide").setExecutor(new CommandHalfHide(this));
        Bukkit.getLogger().info("Плагин включен!");
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info( "Конфиг не найден, создаю новый файл!");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    public void onDisable(){
        Bukkit.getLogger().info("Плагин выключен!");
    }

}
