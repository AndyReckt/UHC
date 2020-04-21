package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BestPVEListener implements Listener {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    @EventHandler
    public void handleEntityDamageEvent(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if(Scenarios.BESTPVE.isEnabled()) {
                    if(!event.isCancelled()) {
                        if(game.getBestPvePlayers().contains(player.getUniqueId())) {
                            game.getBestPvePlayers().remove(player.getUniqueId());
                            player.sendMessage(prefix + ChatColor.RED + "You have been removed from the BestPVE list.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(Scenarios.BESTPVE.isEnabled()) {
                if(killer != null) {
                    if(!game.getBestPvePlayers().contains(killer.getUniqueId())) {
                        game.getBestPvePlayers().add(killer.getUniqueId());
                        killer.sendMessage(prefix + ChatColor.GREEN + "You have been added to the BestPVE list.");
                    }
                }
            }
        }
    }

}
