package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DiamondlessListener implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.DIAMOND_ORE) {
            if(Scenarios.Diamondless.isEnabled()) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(5);
            }
        }
    }

    @EventHandler
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player dyingPlayer = event.getEntity();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(Scenarios.Diamondless.isEnabled()) {
                dyingPlayer.getWorld().dropItemNaturally(dyingPlayer.getLocation(), new ItemStack(Material.DIAMOND));
            }
        }
    }

}
