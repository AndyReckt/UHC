package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


public class VeinMinerListener implements Listener {

    private Game game = Game.getInstance();

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(player.isSneaking()) {
                if(Scenarios.VEINMINER.isEnabled()) {
                    if(block.getType() == Material.DIAMOND_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.DIAMOND_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            if(!Scenarios.BAREBONES.isEnabled() || !Scenarios.DIAMONDLESS.isEnabled()) {
                                if(Scenarios.DOUBLEORES.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND, drops * 2 - 2));
                                } else if(Scenarios.TRIPLEORES.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND, drops * 3 - 3));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND, drops - 1));
                                }

                                if(Scenarios.BLOODDIAMONDS.isEnabled()) {
                                    player.damage(drops - 2);
                                }

                                block.getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(4 + drops - 1);
                            }
                        }
                    } else if(block.getType() == Material.GLOWING_REDSTONE_ORE || block.getType() == Material.REDSTONE_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.REDSTONE_ORE || b2.getType() == Material.GLOWING_REDSTONE_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.REDSTONE, drops * 4 - 4));

                            event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(3 * drops - 1);
                        }
                    } else if(block.getType() == Material.EMERALD_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.EMERALD_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.EMERALD, drops - 1));

                            event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(2 * drops - 1);
                        }
                    } else if(block.getType() == Material.COAL_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE ||
                                    player.getItemInHand().getType() == Material.STONE_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.COAL_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COAL, drops - 1));

                            event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(drops - 1);
                        }
                    } else if(block.getType() == Material.LAPIS_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE ||
                                player.getItemInHand().getType() == Material.STONE_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.LAPIS_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.INK_SACK, drops * 4 - 4, (short) 4));

                            event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(2 * drops - 1);
                        }
                    } else if(block.getType() == Material.IRON_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE ||
                                player.getItemInHand().getType() == Material.STONE_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.IRON_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            if(Scenarios.DOUBLEORES.isEnabled()) {
                                if(Scenarios.CUTCLEAN.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT, drops * 2 - 2));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_ORE, drops * 2 - 2));
                                }
                            } else if(Scenarios.TRIPLEORES.isEnabled()) {
                                if(Scenarios.CUTCLEAN.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT, drops * 3 - 3));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_ORE, drops * 3 - 3));
                                }
                            } else {
                                if(Scenarios.CUTCLEAN.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT, drops - 1));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_ORE, drops - 1));
                                }
                            }

                            if(Scenarios.CUTCLEAN.isEnabled()) {
                                block.getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(2 * drops - 1);
                            }
                        }
                    } else if(block.getType() == Material.GOLD_ORE) {
                        if(player.getItemInHand().getType() == Material.IRON_PICKAXE
                                || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                            int drops = 1;

                            int minX = block.getX() - 2;
                            int minY = block.getY() - 2;
                            int minZ = block.getZ() - 2;
                            int maxX = block.getX() + 2;
                            int maxY = block.getY() + 2;
                            int maxZ = block.getZ() + 2;
                            for (int counterX = minX; counterX <= maxX; counterX++) {
                                for (int counterY = minY; counterY <= maxY; counterY++) {
                                    for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
                                        Block b2 = Bukkit.getWorld("uhc_world").getBlockAt(counterX, counterY, counterZ);
                                        if (b2.getType() == Material.GOLD_ORE) {
                                            b2.setType(Material.AIR);
                                            b2.getState().update();
                                            drops++;
                                        }
                                    }
                                }
                            }

                            event.setCancelled(true);

                            block.setType(Material.AIR);
                            block.getState().update();

                            if(Scenarios.DOUBLEORES.isEnabled()) {
                                if(Scenarios.CUTCLEAN.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT, drops * 2 - 2));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_ORE, drops * 2 - 2));
                                }
                            } else if(Scenarios.TRIPLEORES.isEnabled()) {
                                if(Scenarios.CUTCLEAN.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT, drops * 3 - 3));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_ORE, drops * 3 - 3));
                                }
                            } else {
                                if(Scenarios.CUTCLEAN.isEnabled()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT, drops - 1));
                                } else {
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_ORE, drops - 1));
                                }
                            }

                            if(Scenarios.CUTCLEAN.isEnabled()) {
                                block.getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(3 * drops - 1);
                            }
                        }
                    }
                }
            } else {
                if(block.getType() == Material.GOLD_ORE) {
                    event.setCancelled(true);
                    block.setType(Material.AIR);

                    if(Scenarios.CUTCLEAN.isEnabled()) {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
                        block.getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(3);
                    } else {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_ORE));
                    }
                } else if(block.getType() == Material.IRON_ORE) {
                    event.setCancelled(true);
                    block.setType(Material.AIR);

                    if(Scenarios.CUTCLEAN.isEnabled()) {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
                        block.getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(2);
                    } else {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_ORE));
                    }
                }
            }
        }
    }

}


