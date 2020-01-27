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

public class CommandHideAll implements Listener, CommandExecutor {

    SkyHider plugin;
    public CommandHideAll(SkyHider plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            if (sender.hasPermission("skyhider.hide")) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("off")) {
                        if ((plugin.getConfig().getString("players." + sender.getName()) == null)
                                || ((plugin.getConfig().getString("players." + sender.getName() + ".enabled")).equalsIgnoreCase("true"))) {
                            plugin.getConfig().set("players." + sender.getName() + ".enabled", false);
                            plugin.saveConfig();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                player.showPlayer(plugin, p);
                            }
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(plugin, player);
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("on")) {
                        if ((plugin.getConfig().getString("players." + sender.getName()) == null)
                                || (plugin.getConfig().getString("players." + sender.getName() + ".enabled")).equalsIgnoreCase("false")) {
                            plugin.getConfig().set("players." + sender.getName() + ".enabled", true);
                            plugin.getConfig().set("players." + sender.getName() + ".nickname", sender.getName());
                            plugin.saveConfig();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                player.hidePlayer(plugin, p);
                            }
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.hidePlayer(plugin, player);
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
        sender.sendMessage("Режим невидимости hide all был переключен...");
        return args.length > 0;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event){
        if(plugin.getConfig().getString("players." + event.getPlayer().getName() + ".enabled") != null){
            if (plugin.getConfig().getString("players." + event.getPlayer().getName() + ".enabled").equalsIgnoreCase("true")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.canSee(event.getPlayer())) {
                        event.getPlayer().hidePlayer(plugin, p);
                        p.hidePlayer(plugin, event.getPlayer());
                    }
                }
            }
        }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(plugin.getConfig().getString("players." + p.getName() + ".enabled") != null){
                    if (plugin.getConfig().getString("players." + p.getName() + ".enabled").equalsIgnoreCase("true")) {
                        if (event.getPlayer().canSee(p)) {
                            event.getPlayer().hidePlayer(plugin, p);
                        }
                        if (p.canSee(event.getPlayer())) {
                            p.hidePlayer(plugin, event.getPlayer());
                        }
                    }
                }
            }
    }
}
