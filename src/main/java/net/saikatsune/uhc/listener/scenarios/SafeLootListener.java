package net.saikatsune.uhc.listener.scenarios;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.handler.ItemHandler;
import net.saikatsune.uhc.handler.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SafeLootListener implements Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    private final HashMap<Location, UUID> safeLoot = new HashMap<>();

    @EventHandler
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(Scenarios.SafeLoot.isEnabled()) {
                event.getDrops().clear();
                new TimeBomb(player.getName(), player.getKiller(), player.getLocation(), player.getLevel() * 10,
                        player.getInventory().getContents(), player.getInventory().getArmorContents(), game);
            }
        }
    }

    @EventHandler
    public void handlePlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(game.getPlayers().contains(player.getUniqueId())) {
                if (Scenarios.SafeLoot.isEnabled()) {
                    if(!Scenarios.TimeBomb.isEnabled()) {
                        try {
                            Block block = event.getClickedBlock();
                            if (safeLoot.containsKey(block.getLocation())) {
                                if(!game.getGameManager().isTeamGame()) {
                                    if (safeLoot.get(block.getLocation()) != player.getUniqueId()) {
                                        event.setCancelled(true);
                                        player.sendMessage(prefix + ChatColor.RED + "The loot in the chest does not belong to you.");
                                    }
                                } else {
                                    TeamHandler teamHandler = game.getTeamManager().getTeams().get(game.getTeamNumber().get(player.getUniqueId()));

                                    if ((safeLoot.get(block.getLocation()) != player.getUniqueId()) &&
                                            !teamHandler.getTeamMembers().contains(safeLoot.get(block.getLocation()))) {
                                        event.setCancelled(true);
                                        player.sendMessage(prefix + ChatColor.RED + "The loot in the chest does not belong to you.");
                                    }
                                }
                            }
                        } catch (Exception ignored) {}
                    } else {
                        Scenarios.SafeLoot.setEnabled(false);
                        Bukkit.broadcastMessage(prefix + ChatColor.RED + "SafeLoot has been disabled because TimeBomb is already active.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {

    }

    public class TimeBomb {
        public TimeBomb(String name, Player killer, Location location, int xp, ItemStack[] inv, ItemStack[] armor, Game game) {
            location.getWorld().spawn(location, ExperienceOrb.class).setExperience(xp);

            Block chest1 = location.getBlock();
            chest1.setType(Material.CHEST);
            Block chest2 = chest1.getRelative(BlockFace.NORTH);
            chest2.setType(Material.CHEST);

            safeLoot.put(chest1.getLocation(), killer.getUniqueId());
            safeLoot.put(chest2.getLocation(), killer.getUniqueId());

            chest1.getRelative(BlockFace.UP).setType(Material.AIR);
            chest2.getRelative(BlockFace.UP).setType(Material.AIR);

            final Chest chest = (Chest) chest1.getState();
            for (ItemStack stack : armor) {
                if ((stack != null) && (stack.getType() != Material.AIR)) {
                    chest.getInventory().addItem(stack);
                }
            }

            chest.getInventory().addItem(new ItemHandler(Material.GOLDEN_APPLE).setDisplayName(ChatColor.GOLD + "Golden Head").build());

            for (ItemStack stack : inv) {
                if ((stack != null) && (stack.getType() != Material.AIR)) {
                    chest.getInventory().addItem(stack);
                }
            }

            if(game.getServer().getPluginManager().getPlugin("HologramAPI") != null) {
                Hologram hologram = HologramAPI.createHologram(chest.getLocation().clone().add(0.5, 1, 0), ChatColor.GREEN + "30");
                hologram.spawn();

                Hologram above = hologram.addLineAbove(game.getmColor() + name + "'s " + game.getsColor() + "corpse");


                new BukkitRunnable() {
                    int duration = 30;

                    public void run() {
                        if (duration == 0) {
                            if (hologram.isSpawned()) hologram.despawn();
                            if (above.isSpawned()) above.despawn();
                            if (chest.getLocation().getBlock().getType() == Material.CHEST) chest.getInventory().clear();
                            location.getWorld().createExplosion(location, 10.0F);
                            location.getWorld().strikeLightning(location);
                            this.cancel();

                            Bukkit.broadcastMessage(game.getPrefix() + mColor + "[SafeLoot] " + name + "'s " + sColor + "corpse has exploded.");
                            return;
                        }

                        duration--;

                        if (duration > 20) {
                            hologram.setText(ChatColor.GREEN.toString() + duration);
                        } else if (duration > 10) {
                            hologram.setText(ChatColor.GOLD.toString() + duration);
                        } else {
                            hologram.setText(ChatColor.RED.toString() + duration);
                        }
                    }
                }.runTaskTimer(game, 20, 20);
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        location.getWorld().createExplosion(location, 10.0F);
                        location.getWorld().strikeLightning(location);
                        Bukkit.broadcastMessage(game.getPrefix() + mColor + "[SafeLoot] " + name + "'s " + sColor + "corpse has exploded.");
                    }
                }.runTaskLater(game, 30 * 20);
            }
        }
    }
}
