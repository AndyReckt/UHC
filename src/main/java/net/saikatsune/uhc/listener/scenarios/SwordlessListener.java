package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SwordlessListener implements Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    @EventHandler
    public void handlePlayerCraftEvent(PrepareItemCraftEvent event) {
        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            switch (event.getRecipe().getResult().getType()) {
                case WOOD_SWORD: case GOLD_SWORD: case STONE_SWORD: case IRON_SWORD: case DIAMOND_SWORD:
                    if(Scenarios.Swordless.isEnabled()) {
                        event.getInventory().setResult(new ItemStack(Material.AIR));
                    }
                break;
            }
        }
    }

    @EventHandler
    public void handlePlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK
            || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                switch (player.getItemInHand().getType()) {
                    case WOOD_SWORD: case GOLD_SWORD: case STONE_SWORD: case IRON_SWORD: case DIAMOND_SWORD:
                        if(player.getInventory().contains(player.getItemInHand().getType())) {
                            if(Scenarios.Swordless.isEnabled()) {
                                player.getInventory().remove(player.getItemInHand());
                            }
                        }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void handlePlayerInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getClickedInventory() != null) {
            if(event.getCurrentItem() != null) {
                if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                    switch (event.getCurrentItem().getType()) {
                        case WOOD_SWORD: case GOLD_SWORD: case STONE_SWORD: case IRON_SWORD: case DIAMOND_SWORD:
                            if(player.getInventory().contains(player.getItemInHand().getType())) {
                                if(Scenarios.Swordless.isEnabled()) {
                                    player.getInventory().remove(player.getItemInHand());
                                }
                            }
                        break;
                    }
                }
            }
        }
    }

}
