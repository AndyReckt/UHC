package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BleedingSweetsListener implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player dyingPlayer = event.getEntity();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(Scenarios.BleedingSweets.isEnabled()) {
                dyingPlayer.getWorld().dropItem(dyingPlayer.getLocation(), new ItemStack(Material.DIAMOND));
                dyingPlayer.getWorld().dropItem(dyingPlayer.getLocation(), new ItemStack(Material.GOLD_INGOT, 5));
                dyingPlayer.getWorld().dropItem(dyingPlayer.getLocation(), new ItemStack(Material.ARROW, 16));
                dyingPlayer.getWorld().dropItem(dyingPlayer.getLocation(), new ItemStack(Material.STRING));
            }
        }
    }

}
