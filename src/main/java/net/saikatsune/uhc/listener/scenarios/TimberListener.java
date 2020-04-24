package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TimberListener implements Listener {

    private Game game = Game.getInstance();

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if(game.getSpectators().contains(player)) event.setCancelled(true);

        if(Scenarios.Timber.isEnabled()) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (event.isCancelled()) return;

            if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2) {
                Block up = event.getBlock();
                while (up.getType() == Material.LOG || up.getType() == Material.LOG_2) {
                    event.getPlayer().getInventory().addItem(new ItemStack(up.getType(), 1, up.getData()));
                    up.setType(Material.AIR);
                    up = up.getLocation().clone().add(0, 1, 0).getBlock();
                }
            }
        }

    }

}
