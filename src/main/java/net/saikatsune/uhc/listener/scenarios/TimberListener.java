package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TimberListener implements Listener {

    private final Game game = Game.getInstance();

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
                player.getInventory().addItem(new ItemStack(Material.LOG));

                this.loopBlockBreak(event.getBlock(), player);
            }
        }

    }

    private void loopBlockBreak(Block block, Player player) {
        BlockFace[] values;
        for (int length = (values = BlockFace.values()).length, i = 0; i < length; i++) {
            BlockFace blockface = values[i];
            if (block.getRelative(blockface).getType().equals(Material.LOG) || block.getRelative(blockface).getType().equals(Material.LOG_2)) {
                Block block2 = block.getRelative(blockface);
                player.getInventory().addItem(new ItemStack(Material.LOG));
                block2.setType(Material.AIR);
                this.loopBlockBreak(block2, player);
            }
        }
    }

}
