package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.enums.ServerVersion;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OreFrenzyListener implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler(priority = EventPriority.LOW)
    public void handleBlockBreakEvent(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if(Scenarios.OreFrenzy.isEnabled()) {

            Location clone = new Location(event.getBlock().getWorld(),
                    event.getBlock().getLocation().getBlockX() + 0.5D, event.getBlock().getLocation().getBlockY(),
                    event.getBlock().getLocation().getBlockZ() + 0.5D);

            Location legacy = event.getBlock().getLocation();

            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            if (event.isCancelled()) return;
            switch (event.getBlock().getType()) {
                case LAPIS_ORE:
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getState().update();
                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.POTION, 1, (short) 16389));
                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.POTION, 1, (short) 16389));
                    }
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(3);
                    break;
                case EMERALD_ORE:
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getState().update();
                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.ARROW, 32));
                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.ARROW, 32));
                    }
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(4);
                    break;
                case REDSTONE_ORE:
                case GLOWING_REDSTONE_ORE:
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getState().update();
                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.BOOK));
                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.BOOK));
                    }
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(2);
                    break;
                case DIAMOND_ORE:
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getState().update();
                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                        if(!Scenarios.Diamondless.isEnabled()) {
                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.DIAMOND));
                        }
                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.EXP_BOTTLE, 4));
                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                        if(!Scenarios.Diamondless.isEnabled()) {
                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.DIAMOND));
                        }
                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.EXP_BOTTLE, 4));
                    }
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(5);
                    break;
                case QUARTZ_ORE:
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getState().update();
                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.TNT));
                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.TNT));
                    }
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(3);
                    break;
            }
        }

    }

}
