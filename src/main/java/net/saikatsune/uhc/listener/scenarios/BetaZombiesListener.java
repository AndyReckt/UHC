package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BetaZombiesListener implements Listener {

    private Game game = Game.getInstance();

    @EventHandler
    public void handleEntityDeathEvent(EntityDeathEvent event) {
        if(event.getEntity() instanceof Zombie) {
            if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if(Scenarios.BETAZOMBIES.isEnabled()) {
                    event.getDrops().clear();

                    event.getDrops().add(new ItemStack(Material.FEATHER));
                }
            }
        }
    }

}
