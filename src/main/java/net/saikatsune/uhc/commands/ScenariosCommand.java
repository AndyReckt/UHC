package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScenariosCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("scenarios")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(game.getScenariosInList().size() > 0) {
                    player.openInventory(Scenarios.getExplanations());
                } else {
                    player.sendMessage(game.getPrefix() + ChatColor.RED + "There are currently no scenarios enabled.");
                }
            }
        }
        return false;
    }
}
