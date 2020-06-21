package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeaderboardsCommand implements CommandExecutor, Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("leaderboards")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;

                if(player.getName().equalsIgnoreCase("Saikatsune")) {
                    if(game.isDatabaseActive()) {
                        Bukkit.getScheduler().runTaskAsynchronously(game, () ->
                                game.getInventoryHandler().handleLeaderboardsInventory(player));
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "Stats are currently disabled.");
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "This command is not accessible for players yet.");
                }
            }
        }
        return false;
    }

    @EventHandler
    public void handleInventoryClickEvent(InventoryClickEvent event) {
        if(event.getClickedInventory() != null) {
            if(event.getCurrentItem() != null) {
                if(event.getClickedInventory().getName().contains("Leaderboards")) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
