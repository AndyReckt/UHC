package net.saikatsune.uhc.support;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.ServerVersion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class LegacySupport implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();

        String spigotVersion = game.getServerVersion();

        Location clone = new Location(event.getBlock().getWorld(),
                event.getBlock().getLocation().getBlockX() + 0.5D, event.getBlock().getLocation().getBlockY(),
                event.getBlock().getLocation().getBlockZ() + 0.5D);

        Location legacy = block.getLocation();

        if(block.getType() == Material.STONE) {
            if(block.getData() == (byte) 1 || (block.getData() == (byte) 2) ||
                    block.getData() == (byte) 3 || block.getData() == (byte) 4 ||
                    block.getData() == (byte) 5 || block.getData() == (byte) 6) {
                event.setCancelled(true);
                block.setType(Material.AIR);
                block.getState().update();

                if(spigotVersion.equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                    block.getWorld().dropItemNaturally(clone, new ItemStack(Material.COBBLESTONE));
                } else if(spigotVersion.equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                    block.getWorld().dropItemNaturally(legacy, new ItemStack(Material.COBBLESTONE));
                }
            }
        }
    }

}
