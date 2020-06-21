package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class HorselessListener implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler
    public void handleVehicleEnterEvent(VehicleEnterEvent event) {
        if(event.getVehicle().getType() == EntityType.HORSE) {
            if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if(Scenarios.Horseless.isEnabled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
