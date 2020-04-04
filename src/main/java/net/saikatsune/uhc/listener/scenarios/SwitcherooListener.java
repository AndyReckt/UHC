package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class SwitcherooListener implements Listener {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    private HashMap<Player, Location> newLocation = new HashMap<>();

    @EventHandler
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                    if(!game.isInGrace()) {
                        Player shooter = (Player) arrow.getShooter();
                        Player shot = (Player) event.getEntity();

                        if(Scenarios.SWITCHEROO.isEnabled()) {
                            newLocation.put(shooter, shot.getLocation());
                            newLocation.put(shot, shooter.getLocation());

                            shooter.teleport(newLocation.get(shooter));
                            shooter.sendMessage(prefix + ChatColor.YELLOW + "You have switched places with " + shot.getName() + ".");

                            shot.teleport(newLocation.get(shot));
                            shot.sendMessage(prefix + ChatColor.YELLOW + "You have switched places with " + shooter.getName() + ".");
                        }
                    }
                }
            }
        }
    }

}
