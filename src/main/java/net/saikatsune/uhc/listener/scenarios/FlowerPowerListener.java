package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.enums.ServerVersion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@SuppressWarnings("deprecation")
public class FlowerPowerListener implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Material material = event.getBlock().getType();

        Random random = new Random();

        if(Scenarios.FlowerPower.isEnabled()) {

            Location clone = new Location(event.getBlock().getWorld(),
                    event.getBlock().getLocation().getBlockX() + 0.5D, event.getBlock().getLocation().getBlockY(),
                    event.getBlock().getLocation().getBlockZ() + 0.5D);

            Location legacy = event.getBlock().getLocation();

            if(material == Material.getMaterial(38) || material == Material.YELLOW_FLOWER || material == Material.CROPS ||
                material == Material.DOUBLE_PLANT) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getState().update();

                ItemStack toDrop = new ItemStack(Material.values()
                        [random.nextInt(Material.values().length)], random.nextInt(64));

                if(!toDrop.equals(new ItemStack(Material.AIR))) {
                    if(!toDrop.equals(new ItemStack(Material.BEDROCK))) {
                        if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                            event.getBlock().getWorld().dropItemNaturally(clone, toDrop);
                        } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                            event.getBlock().getWorld().dropItemNaturally(legacy, toDrop);
                        }
                    }
                }
            }
        }
    }

}
