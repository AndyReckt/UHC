package net.saikatsune.uhc.gamestate.states;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class IngameState extends GameState {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    public void start() {
        game.getWorldManager().createBorderLayer("uhc_world", game.getConfigManager().getBorderSize(),4, null);

        game.getGameManager().playSound();

        game.setChatMuted(true);

        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                allPlayers.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, game.getConfigManager().getStarterFood()));
            }
        }

        game.getTimeTask().runTask();

        if(Scenarios.CutClean.isEnabled()) {
            Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Remember: Sheep also drop leather.");
        }

        game.getButcherTask().run();
        game.getRelogTask().startTask();

        Bukkit.getWorld("uhc_world").setTime(0);

        game.getGameManager().setWhitelisted(false);

        if(Scenarios.BestPVE.isEnabled()) {
            game.getGameManager().startBestPveTimer();

            for (UUID allPlayers : game.getPlayers()) {
                game.getBestPvePlayers().add(allPlayers);

                if(allPlayers != null) {
                    Player allOnlinePlayers = Bukkit.getPlayer(allPlayers);

                    allOnlinePlayers.sendMessage(prefix + ChatColor.GREEN + "You have been added to the BestPVE list.");
                }
            }
        }
    }

    public void stop() {

    }
}
