package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChatCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("mutechat")) {
            if(sender instanceof Player) {
                if(game.isChatMuted()) {
                    game.setChatMuted(false);
                    Bukkit.broadcastMessage(prefix + mColor + "Global Chat " + sColor + "has been " + ChatColor.GREEN + "enabled" + sColor + "!");
                } else {
                    game.setChatMuted(true);
                    Bukkit.broadcastMessage(prefix + mColor + "Global Chat " + sColor + "has been " + ChatColor.RED + "disabled" + sColor + "!");
                }
            }
        }
        return false;
    }
}
