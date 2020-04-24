package net.saikatsune.uhc.tasks;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@SuppressWarnings("deprecation")
public class RelogTask {

    private Game game = Game.getInstance();

    private int taskID;

    public void startTask() {
        taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(game, new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (UUID loggedOutPlayers : game.getLoggedOutPlayers()) {
                        game.getLogoutTimer().put(loggedOutPlayers, game.getLogoutTimer().get(loggedOutPlayers) - 1);

                        if(game.getLogoutTimer().get(loggedOutPlayers) == 0) {
                            game.getLoggedOutPlayers().remove(loggedOutPlayers);

                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(loggedOutPlayers);

                            Bukkit.broadcastMessage(ChatColor.RED + offlinePlayer.getName() + ChatColor.GRAY +
                                    "[" + game.getPlayerKills().get(offlinePlayer.getUniqueId()) + "] " + ChatColor.YELLOW +
                                    "was disconnected for too long and has been disqualified.");

                            try {
                                game.getGameManager().dropPlayerDeathInventory(offlinePlayer.getUniqueId());

                                if(Scenarios.BleedingSweets.isEnabled()) {
                                    game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItemNaturally(
                                            game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemStack(Material.DIAMOND));
                                    game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItemNaturally(
                                            game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemStack(Material.GOLD_INGOT, 5));
                                    game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItemNaturally(
                                            game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemStack(Material.ARROW, 16));
                                    game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItemNaturally(
                                            game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemStack(Material.STRING));
                                }

                                if(Scenarios.Goldless.isEnabled()) {
                                    game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItemNaturally(
                                            game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemStack(Material.GOLD_INGOT, 8));
                                }

                                if(Scenarios.Diamondless.isEnabled()) {
                                    game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItemNaturally(
                                            game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemStack(Material.DIAMOND));
                                }
                            } catch (Exception ignored) {

                            }

                            game.getGameManager().removeCombatVillager(game.getCombatVillagerUUID().get(offlinePlayer.getUniqueId()));
                            game.getLoggedPlayers().remove(offlinePlayer.getUniqueId());
                            game.getWhitelisted().remove(offlinePlayer.getUniqueId());
                            game.getPlayers().remove(offlinePlayer.getUniqueId());

                            game.getDeadPlayersByUUID().add(offlinePlayer.getUniqueId());

                            if(game.isDatabaseActive()) {
                                game.getDatabaseManager().addDeaths(offlinePlayer, 1);
                            }

                            if(game.getGameManager().isTeamGame()) {
                                game.getGameManager().removeDeadTeams();
                            }

                            game.getGameManager().checkWinner();
                        }
                    }
                } catch (Exception ignored) {

                }
            }
        }, 0, 20);
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
