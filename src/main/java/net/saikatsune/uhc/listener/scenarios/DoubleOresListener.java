package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.enums.Scenarios;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DoubleOresListener implements Listener {

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {

        switch (event.getBlock().getType()) {
            case IRON_ORE:
                if(Scenarios.DoubleOres.isEnabled()) {
                    if(Scenarios.CutClean.isEnabled()) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, 2));
                    } else {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.IRON_ORE, 2));
                    }
                } else if(Scenarios.TripleOres.isEnabled()) {
                    if(Scenarios.CutClean.isEnabled()) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, 3));
                    } else {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.IRON_ORE, 3));
                    }
                }
                break;

            case GOLD_ORE:
                if(Scenarios.DoubleOres.isEnabled()) {
                    if(Scenarios.CutClean.isEnabled()) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, 2));
                    } else {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_ORE, 2));
                    }
                } else if(Scenarios.TripleOres.isEnabled()) {
                    if(Scenarios.CutClean.isEnabled()) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, 3));
                    } else {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_ORE, 3));
                    }
                }
                break;

            case DIAMOND_ORE:
                if(Scenarios.DoubleOres.isEnabled()) {
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.DIAMOND, 2));
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(6);
                } else if(Scenarios.TripleOres.isEnabled()) {
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.DIAMOND, 3));
                    event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(12);
                }
                break;
        }

    }

}
