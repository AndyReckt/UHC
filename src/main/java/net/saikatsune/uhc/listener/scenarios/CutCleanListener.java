package net.saikatsune.uhc.listener.scenarios;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.enums.ServerVersion;
import org.bukkit.*;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CutCleanListener implements Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(game.getSpectators().contains(player)) event.setCancelled(true);

        if(Scenarios.CutClean.isEnabled()) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            if (event.isCancelled()) return;

            Location clone = new Location(event.getBlock().getWorld(),
                    event.getBlock().getLocation().getBlockX() + 0.5D, event.getBlock().getLocation().getBlockY(),
                    event.getBlock().getLocation().getBlockZ() + 0.5D);

            Location legacy = event.getBlock().getLocation();

            switch (event.getBlock().getType()) {
                case IRON_ORE:
                    if ((player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) && (player.getItemInHand().getType() != Material.IRON_PICKAXE) &&
                            (player.getItemInHand().getType() != Material.STONE_PICKAXE)) {
                        event.getBlock().setType(Material.AIR);
                        return;
                    }
                    if(!Scenarios.VeinMiner.isEnabled()) {
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getState().update();
                        if(!Scenarios.Limitations.isEnabled()) {
                            if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                                if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                    event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.IRON_INGOT));
                                } else if(Scenarios.DoubleOres.isEnabled()) {
                                    event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.IRON_INGOT, 2));
                                } else {
                                    event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.IRON_INGOT, 3));
                                }
                            } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                                if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                    event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.IRON_INGOT));
                                } else if(Scenarios.DoubleOres.isEnabled()) {
                                    event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.IRON_INGOT, 2));
                                } else {
                                    event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.IRON_INGOT, 3));
                                }
                            }
                        } else {
                            game.getLimitationsListener().getIronMined().putIfAbsent(player.getUniqueId(), 0);
                            if(game.getLimitationsListener().getIronMined().get(player.getUniqueId()) >= 64) {
                                event.getBlock().setType(Material.AIR);
                                player.sendMessage(prefix + ChatColor.RED + "You can only mine 64 iron!");
                                return;
                            } else if(game.getLimitationsListener().getIronMined().get(player.getUniqueId()) < 64) {
                                if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                                    if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.IRON_INGOT));
                                    } else if(Scenarios.DoubleOres.isEnabled()) {
                                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.IRON_INGOT, 2));
                                    } else {
                                        event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.IRON_INGOT, 3));
                                    }
                                } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                                    if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.IRON_INGOT));
                                    } else if(Scenarios.DoubleOres.isEnabled()) {
                                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.IRON_INGOT, 2));
                                    } else {
                                        event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.IRON_INGOT, 3));
                                    }
                                }
                                game.getLimitationsListener().getIronMined().put(player.getUniqueId(), game.getLimitationsListener().getIronMined().get(player.getUniqueId()) + 1);
                            }
                        }
                        event.getBlock().getWorld().spawn(clone, ExperienceOrb.class).setExperience(2);
                    }
                    break;
                case GOLD_ORE:
                    if ((player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) && (player.getItemInHand().getType() != Material.IRON_PICKAXE)) {
                        event.getBlock().setType(Material.AIR);
                        return;
                    }
                    if(!Scenarios.VeinMiner.isEnabled()) {
                        if(!Scenarios.Goldless.isEnabled()) {
                            event.getBlock().setType(Material.AIR);
                            event.getBlock().getState().update();
                            if(!Scenarios.Limitations.isEnabled()) {
                                if(!Scenarios.Barebones.isEnabled()) {
                                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                                        if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.GOLD_INGOT));
                                        } else if(Scenarios.DoubleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.GOLD_INGOT, 2));
                                        } else {
                                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.GOLD_INGOT, 3));
                                        }
                                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                                        if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.GOLD_INGOT));
                                        } else if(Scenarios.DoubleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.GOLD_INGOT, 2));
                                        } else {
                                            event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.GOLD_INGOT, 3));
                                        }
                                    }
                                } else {
                                    event.getBlock().setType(Material.AIR);
                                    player.sendMessage(prefix + ChatColor.RED + "You can only mine iron!");
                                    return;
                                }
                            } else {
                                game.getLimitationsListener().getGoldMined().putIfAbsent(player.getUniqueId(), 0);
                                if(game.getLimitationsListener().getGoldMined().get(player.getUniqueId()) >= 32) {
                                    event.getBlock().setType(Material.AIR);
                                    player.sendMessage(prefix + ChatColor.RED + "You can only mine 32 gold!");
                                    return;
                                } else if(game.getLimitationsListener().getGoldMined().get(player.getUniqueId()) < 32) {
                                    if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_8_X.toString())) {
                                        if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.GOLD_INGOT));
                                        } else if(Scenarios.DoubleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.GOLD_INGOT, 2));
                                        } else {
                                            event.getBlock().getWorld().dropItemNaturally(clone, new ItemStack(Material.GOLD_INGOT, 3));
                                        }
                                    } else if(game.getServerVersion().equalsIgnoreCase(ServerVersion.V1_7_X.toString())) {
                                        if((!Scenarios.DoubleOres.isEnabled()) && !Scenarios.TripleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.GOLD_INGOT));
                                        } else if(Scenarios.DoubleOres.isEnabled()) {
                                            event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.GOLD_INGOT, 2));
                                        } else {
                                            event.getBlock().getWorld().dropItemNaturally(legacy, new ItemStack(Material.GOLD_INGOT, 3));
                                        }
                                    }
                                    game.getLimitationsListener().getGoldMined().put(player.getUniqueId(), game.getLimitationsListener().getGoldMined().get(player.getUniqueId()) + 1);
                                }
                            }
                            event.getBlock().getWorld().spawn(clone, ExperienceOrb.class).setExperience(4);
                        }
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void handleEntityDeathEvent(EntityDeathEvent event) {
        if(Scenarios.CutClean.isEnabled()) {
            switch (event.getEntityType()) {
                case COW:
                case MUSHROOM_COW:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.LEATHER, 1));
                    event.getDrops().add(new ItemStack(Material.COOKED_BEEF, 3));
                    break;
                case PIG:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
                    break;
                case CHICKEN:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 1));
                    event.getDrops().add(new ItemStack(Material.FEATHER, 1));
                    break;
                case HORSE:
                case SHEEP:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.LEATHER, 1));
                    break;
                case RABBIT:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.COOKED_RABBIT, 1));
                    break;
            }
        }
    }

}
