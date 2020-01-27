package ru.vlad0sminer.hider.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.vlad0sminer.hider.SkyHider;

public class CommandHalfHide implements Listener, CommandExecutor {

    SkyHider plugin;
    public CommandHalfHide(SkyHider plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            if (sender.hasPermission("skyhider.halfhide")) {
                Player player = (Player) sender;
                if (args.length > 0){
                    if(args[0].equalsIgnoreCase("on")) {
                        if ((plugin.getConfig().getString("players." + sender.getName() + ".half_enabled") == null) ||
                                (plugin.getConfig().getString("players." + sender.getName() + ".half_enabled")).equalsIgnoreCase("false")) {
                            plugin.getConfig().set("players." + sender.getName() + ".half_enabled", true);
                            plugin.getConfig().set("players." + sender.getName() + ".enabled", false);
                            plugin.getConfig().set("players." + sender.getName() + ".nickname", sender.getName());
                            plugin.saveConfig();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.hidePlayer(plugin, player);
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("off")) {
                        if (plugin.getConfig().getString("players." + sender.getName() + ".half_enabled") != null) {
                            plugin.getConfig().set("players." + sender.getName() + ".half_enabled", false);
                            plugin.saveConfig();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(plugin, player);
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage("Недостаточно прав!");
            }
        } else {
            sender.sendMessage("Команда не для консоли!");
        }
        sender.sendMessage("Режим невидимости half hide был переключен...");
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event){
        if(plugin.getConfig().getString("players." + event.getPlayer().getName() + ".half_enabled") != null){
            if (plugin.getConfig().getString("players." + event.getPlayer().getName() + ".half_enabled").equalsIgnoreCase("true")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.canSee(event.getPlayer())) {
                        p.hidePlayer(plugin, event.getPlayer());
                    }
                }
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if(plugin.getConfig().getString("players." + p.getName() + ".half_enabled") != null){
                if (plugin.getConfig().getString("players." + p.getName() + ".half_enabled").equalsIgnoreCase("true")) {
                    if (event.getPlayer().canSee(p)) {
                        event.getPlayer().hidePlayer(plugin, p);
                    }
                }
            }
        }
    }
}
