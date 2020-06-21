package net.saikatsune.uhc.tasks;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.handler.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@SuppressWarnings("deprecation")
public class RelogTask {

    private final Game game = Game.getInstance();

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

                            try {
                                game.getGameManager().dropPlayerDeathInventory(offlinePlayer.getUniqueId());

                                game.getDeathLocation().get(offlinePlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(offlinePlayer.getUniqueId()), new ItemHandler(Material.GOLDEN_APPLE).setDisplayName(
                                                ChatColor.GOLD + "Golden Head").build());

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
                            } catch (Exception ignored) {}

                            game.getGameManager().disqualifyPlayer(offlinePlayer);
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
