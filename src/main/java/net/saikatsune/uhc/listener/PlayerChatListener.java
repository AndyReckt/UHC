package net.saikatsune.uhc.listener;

import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String specPrefix = game.getConfig().getString("CHAT.SPECTATOR-PREFIX").replace("&", "§");
    private final String hostPrefix = game.getConfig().getString("CHAT.HOST-PREFIX").replace("&", "§");
    private final String modPrefix = game.getConfig().getString("CHAT.MOD-PREFIX").replace("&", "§");

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleASyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(game.isChatMuted()) {
            if(!player.hasPermission("uhc.host")) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.RED + "Global Chat is currently disabled!");
            }
        }

        if(!game.getSpectators().contains(player)) {
            if(game.getGameManager().isTeamGame()) {
                if(game.getTeamNumber().get(player.getUniqueId()) != -1) {
                    if(game.getTeamManager().getTeams().containsKey(game.getTeamNumber().get(player.getUniqueId()))) {
                        if(game.getPlayers().contains(player.getUniqueId())) {

                            String teamPrefix = game.getConfig().getString("CHAT.TEAM-PREFIX").replace("&", "§").
                                    replace("%teamNumber%", String.valueOf(game.getTeamNumber().get(player.getUniqueId())));

                            if(!game.isChatMuted()) {
                                event.setFormat(teamPrefix + ChatColor.WHITE + event.getFormat());
                            } else {
                                if(!player.hasPermission("uhc.host")) {
                                    event.setCancelled(true);
                                    player.sendMessage(prefix + ChatColor.RED + "Global Chat is currently disabled!");
                                } else {
                                    event.setFormat(teamPrefix + ChatColor.WHITE + event.getFormat());
                                }
                            }
                        }
                    }
                }
            }
        }

        if(!player.hasPermission("uhc.host")) {
            if(game.getSpectators().contains(player)) {
                if(!game.isChatMuted()) {
                    event.setFormat(specPrefix + ChatColor.WHITE + event.getFormat());
                } else {
                    if(!player.hasPermission("uhc.host")) {
                        event.setCancelled(true);
                    } else {
                        event.setFormat(specPrefix + ChatColor.WHITE + event.getFormat());
                    }
                }

                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    if(!game.getSpectators().contains(allPlayers)) {
                        event.getRecipients().remove(allPlayers);
                    }
                }
            }
        } else {
            if(!game.getGameHost().equals(player.getName())) {
                if(game.getSpectators().contains(player)) {
                    event.setFormat(modPrefix + ChatColor.WHITE + event.getFormat());
                }
            } else {
                event.setFormat(hostPrefix + ChatColor.WHITE + event.getFormat());
            }
        }

        /*
        if(game.isChatMuted()) {
            if(!player.hasPermission("uhc.host")) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.RED + "Global Chat is currently disabled!");
            }
        }
         */
    }
}
