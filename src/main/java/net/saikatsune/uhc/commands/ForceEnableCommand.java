package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceEnableCommand implements CommandExecutor {

    private Game game = Game.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("forceenable")) {
            if(sender.hasPermission("uhc.host")) {
                if(args.length == 1) {
                    if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                        if(args[0].equalsIgnoreCase("pvp")) {
                            if(game.isInGrace()) {
                                game.getGameManager().endGracePeriod();
                            } else {
                                sender.sendMessage(game.getPrefix() + ChatColor.RED +
                                        "Grace period has already ended.");
                            }
                        } else if(args[0].equalsIgnoreCase("heal")) {
                            if(!game.isFinalHealHappened()) {
                                game.getGameManager().executeFinalHeal();
                                game.setFinalHealHappened(true);
                            } else {
                                sender.sendMessage(game.getPrefix() + ChatColor.RED +
                                        "Final heal already happened.");
                                sender.sendMessage(game.getPrefix() + ChatColor.RED +
                                        "If you want to heal all players use /heal all.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /forceenable pvp");
                            sender.sendMessage(ChatColor.RED + "Usage: /forceenable heal");
                        }
                    } else {
                        sender.sendMessage(game.getPrefix() + ChatColor.RED + "There is currently no game running!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /forceenable pvp");
                    sender.sendMessage(ChatColor.RED + "Usage: /forceenable heal");
                }
            }
        }
        return false;
    }
}
