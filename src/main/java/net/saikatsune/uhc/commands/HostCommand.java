package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HostCommand implements CommandExecutor {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("host")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;

                if(game.getGameHost().equals("None") || game.getGameHost().contains(player.getName())) {
                    if(!game.getGameHost().contains(player.getName())) {
                        game.setGameHost(player.getName());

                        player.sendMessage(prefix + ChatColor.GREEN + "You are now the host of the game.");
                    } else {
                        game.setGameHost("None");

                        player.sendMessage(prefix + ChatColor.RED + "You are no longer the host of the game.");
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + game.getGameHost() + " is already the host of the game!");
                }
            }
        }
        return false;
    }
}
