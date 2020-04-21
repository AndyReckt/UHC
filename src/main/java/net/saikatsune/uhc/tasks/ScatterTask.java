package net.saikatsune.uhc.tasks;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.GameState;
import net.saikatsune.uhc.handler.TeamHandler;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

@SuppressWarnings("deprecation")
public class ScatterTask {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    private String mColor = game.getmColor();
    private String sColor = game.getsColor();

    private int taskID;

    private List<UUID> playersToScatter = new ArrayList<>();
    private List<UUID> playersScattered = new ArrayList<>();

    private List<UUID> queuedPlayers = new ArrayList<>();

    public void runTask() {
        game.setChatMuted(true);

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcastMessage("");
        }

        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                if(!game.getSpectators().contains(allPlayers)) {
                    Random randomLocation = new Random();

                    int xPositive = randomLocation.nextInt(game.getConfigManager().getBorderSize() - 1);
                    int xNegative = -game.getConfigManager().getBorderSize() - 1 + (int) (Math.random() * ((-(-game.getConfigManager().getBorderSize() - 1)) + 1));
                    int x = (Math.random() <= 0.5) ? xPositive : xNegative;


                    int zPositive = randomLocation.nextInt(game.getConfigManager().getBorderSize() - 1);
                    int zNegative = -game.getConfigManager().getBorderSize() - 1 + (int) (Math.random() * ((-(-game.getConfigManager().getBorderSize() - 1)) + 1));
                    int z = (Math.random() <= 0.5) ? zPositive : zNegative;

                    int y = Bukkit.getWorld("uhc_world").getHighestBlockYAt(x, z);

                    Location location = new Location(Bukkit.getWorld("uhc_world"), x, y ,z);

                    game.getGameManager().setScatterLocation(allPlayers, location);

                    playersToScatter.add(allPlayers.getUniqueId());
                }
            }
        }

        Bukkit.broadcastMessage(prefix + sColor + "Starting scatter of all players!");

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(game, new BukkitRunnable() {
            @Override
            public void run() {

                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    if(game.getSpectators().contains(allPlayers)) {
                        playersToScatter.remove(allPlayers.getUniqueId());
                    }
                }

                if(playersToScatter.size() >= 5) {
                    for (int y = 0; y < 5; y++) {
                        queuedPlayers.add(playersToScatter.get(y));
                    }
                } else {
                    queuedPlayers.addAll(playersToScatter);
                }

                for (int i = 0; i < queuedPlayers.size(); i++) {
                    Player toScatter = Bukkit.getPlayer(queuedPlayers.get(i));

                    if(toScatter != null) {
                        toScatter.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -5));
                        toScatter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 127));

                        toScatter.teleport(game.getScatterLocation().get(toScatter));

                        playersToScatter.remove(toScatter.getUniqueId());
                        playersScattered.add(toScatter.getUniqueId());
                    } else {
                        playersToScatter.remove(playersToScatter.get(i));
                    }

                    queuedPlayers.clear();
                }

                if (playersToScatter.size() == 0) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Finished scatter of all players!");

                    if(game.getGameManager().isTeamGame()) {
                        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                            if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                                if(game.getTeamNumber().get(allPlayers.getUniqueId()) == -1) {
                                    game.getTeamManager().createTeam(allPlayers.getUniqueId());
                                }
                            }
                        }

                        for (TeamHandler teamHandler : game.getTeamManager().getTeams().values()) {
                            for (UUID toTeleport : teamHandler.getTeamMembers()) {
                                Player playerToTeleport = Bukkit.getPlayer(toTeleport);
                                if(teamHandler.getTeamLeader() != null) {
                                    if(teamHandler.getTeamMembers().contains(teamHandler.getTeamLeader())) {
                                        playerToTeleport.teleport(Bukkit.getPlayer(teamHandler.getTeamLeader()));
                                    }
                                }
                            }
                        }

                        Bukkit.broadcastMessage(prefix + sColor + "All teams have been teleported to their leaders!");
                    }

                    for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                        if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                            game.getLoggedPlayers().add(allPlayers.getUniqueId());
                            game.getWhitelisted().add(allPlayers.getUniqueId());
                        }
                    }

                    game.getGameManager().setWhitelisted(true);

                    Bukkit.broadcastMessage(prefix + sColor + "All players have been whitelisted!");
                    Bukkit.broadcastMessage(prefix + mColor + "The game starts in 10 seconds!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            game.getGameStateManager().setGameState(GameState.INGAME);
                            Bukkit.broadcastMessage(prefix + mColor + "The game has started. Good Luck!");

                            for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                                for (PotionEffect potionEffect : allPlayers.getActivePotionEffects()) {
                                    allPlayers.removePotionEffect(potionEffect.getType());
                                }
                            }

                            if(Scenarios.GONEFISHING.isEnabled()) {
                                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                                    if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                                        allPlayers.getInventory().addItem(new ItemStack(Material.ANVIL, 20));

                                        allPlayers.setLevel(30000);

                                        ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                                        fishingRod.addUnsafeEnchantment(Enchantment.LUCK, 250);
                                        fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);
                                        fishingRod.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
                                        allPlayers.getInventory().addItem(fishingRod);
                                    }
                                }
                            }

                            if(Scenarios.INFINITEENCHANT.isEnabled()) {
                                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                                    if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                                        allPlayers.setLevel(30000);
                                        allPlayers.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                                        allPlayers.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                        allPlayers.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                        allPlayers.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                    }
                                }
                            }
                        }
                    }.runTaskLater(game, 10 * 20);
                }
            }
        }, 0, 10);
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public List<UUID> getPlayersToScatter() {
        return playersToScatter;
    }

    public List<UUID> getPlayersScattered() {
        return playersScattered;
    }
}
