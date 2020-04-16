package net.saikatsune.uhc.manager;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.GameState;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.handler.TeamHandler;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@SuppressWarnings("deprecation")
public class GameManager {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    private String mColor = game.getmColor();

    private boolean whitelisted;
    private boolean teamGame;

    public void resetPlayer(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.setExp(0);
        player.setMaxHealth(20);
    }

    public void setPlayerState(Player player, PlayerState playerState) {
        game.getPlayerState().put(player, playerState);
        if(playerState == PlayerState.PLAYER) {
            player.setGameMode(GameMode.SURVIVAL);

            for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                allPlayers.showPlayer(player);

                if(!game.getSpectators().contains(allPlayers)) {
                    player.showPlayer(allPlayers);
                }
            }

            for (Player allSpectators : game.getSpectators()) {
                player.hidePlayer(allSpectators);
            }

            for (Player allPlayers : game.getSpectators()) {
                allPlayers.showPlayer(player);
            }

            game.getSpectators().remove(player);

            if(!game.getPlayers().contains(player.getUniqueId())) {
                game.getPlayers().add(player.getUniqueId());
            }
        } else if(playerState == PlayerState.SPECTATOR) {
            player.setGameMode(GameMode.CREATIVE);

            for (Player players : Bukkit.getOnlinePlayers()) {
                if(game.getPlayers().contains(players.getUniqueId())) {
                    players.hidePlayer(player);
                }
            }

            for (Player allPlayers : game.getSpectators()) {
                allPlayers.hidePlayer(player);
                allPlayers.hidePlayer(allPlayers);
            }

            game.getPlayers().remove(player.getUniqueId());
            game.getSpectators().add(player);

            player.teleport(new Location(Bukkit.getWorld("uhc_world"), 0 , 100, 0));

            if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if(!player.hasPermission("uhc.host")) {
                    player.sendMessage(prefix + ChatColor.YELLOW + "You are now spectating the game.");

                    if(!game.getDeadPlayersByUUID().contains(player.getUniqueId())) {
                        if(game.isInGrace()) {
                            player.sendMessage(prefix + ChatColor.YELLOW + "Late-scatter yourself with /latescatter.");
                        }
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "You have already died this game.");
                    }
                }
            }
        }
    }

    public void startBestPveTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(Scenarios.BESTPVE.isEnabled()) {
                            for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                                if(game.getBestPvePlayers().contains(allPlayers.getUniqueId())) {
                                    allPlayers.setMaxHealth(allPlayers.getMaxHealth() + 2);

                                    allPlayers.setHealth(allPlayers.getHealth() + 2);
                                }
                            }
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(game, 0, 20 * 60 * 10);
            }
        }.runTaskLater(game, 20 * 60 * 10);
    }

    public void dropPlayerDeathInventory(UUID playerByUUID) {
        ItemStack[] inventoryStack = game.getDeathInventory().get(playerByUUID);
        ItemStack[] armorStack = game.getDeathArmor().get(playerByUUID);

        for (ItemStack itemStack : inventoryStack) {
            if(itemStack != null) {
                game.getDeathLocation().get(playerByUUID).getWorld().dropItemNaturally(
                  game.getDeathLocation().get(playerByUUID), itemStack
                );
            }
        }

        for (ItemStack itemStack : armorStack) {
            if(itemStack != null) {
                if(itemStack.getType() != Material.AIR) {
                    game.getDeathLocation().get(playerByUUID).getWorld().dropItemNaturally(
                            game.getDeathLocation().get(playerByUUID), itemStack
                    );
                }
            }
        }
    }

    public void dropPlayerDeathInventory(UUID playerByUUID, Player player) {
        ItemStack[] inventoryStack = game.getDeathInventory().get(playerByUUID);
        ItemStack[] armorStack = game.getDeathArmor().get(playerByUUID);

        for (ItemStack itemStack : inventoryStack) {
            if(itemStack != null) {
                game.getDeathLocation().get(playerByUUID).getWorld().dropItemNaturally(
                        player.getLocation(), itemStack
                );
            }
        }

        for (ItemStack itemStack : armorStack) {
            if(itemStack != null) {
                if(itemStack.getType() != Material.AIR) {
                    game.getDeathLocation().get(playerByUUID).getWorld().dropItemNaturally(
                            player.getLocation(), itemStack
                    );
                }
            }
        }
    }

    public void setScatterLocation(Player player, Location location) {
        game.getScatterLocation().put(player, location);
    }

    public void spawnCombatVillager(Player player) {
        Villager villager = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        villager.setCustomName(ChatColor.RED + "[CombatLogger] " + player.getName());

        game.registerPlayerDeath(player);
        game.getPlayerNameBoundToVillager().put(villager, player.getUniqueId());

        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 127));

        villager.setRemoveWhenFarAway(false);

        game.getCombatVillagerUUID().put(player.getUniqueId(), villager.getUniqueId());
    }

    public void removeCombatVillager(UUID villagerUUID) {
        World uhcWorld = Bukkit.getWorld("uhc_world");
        World uhcNether = Bukkit.getWorld("uhc_nether");

        for (Entity entity : uhcWorld.getEntities()) {
            if(entity instanceof Villager) {
                if(entity.getUniqueId().equals(villagerUUID)) {
                    entity.remove();
                }
            }
        }

        for (Entity entity : uhcNether.getEntities()) {
            if(entity instanceof Villager) {
                if(entity.getUniqueId().equals(villagerUUID)) {
                    entity.remove();
                }
            }
        }
    }

    //Temporary fix
    public void removeDeadTeams() {
        for (TeamHandler teamHandler : game.getTeamManager().getTeams().values()) {
            if(teamHandler.getTeamMembers().size() == 0) {
                game.getTeamManager().getTeams().remove(teamHandler.getTeamNumber());

                game.getGameManager().checkWinner();
            } else {
                for (int i = 0; i < teamHandler.getTeamMembers().size(); i++) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(teamHandler.getTeamMembers().get(i));

                    if(!game.getPlayers().contains(offlinePlayer.getUniqueId())) {
                        teamHandler.getTeamMembers().remove(offlinePlayer.getUniqueId());

                        if(teamHandler.getTeamMembers().size() == 0) {
                            game.getTeamManager().getTeams().remove(teamHandler.getTeamNumber());

                            game.getGameManager().checkWinner();
                        }
                    }
                }
            }
        }
    }

    public void scatterPlayer(Player player, World world, int radius) {

        Random random = new Random();

        int x = random.nextInt(radius - 1);
        int z = random.nextInt(radius - 1);
        int y = Bukkit.getWorld(world.getName()).getHighestBlockYAt(x, z);

        Location location = new Location(Bukkit.getWorld(world.getName()), x, y ,z);

        player.teleport(location);
    }

    public void checkWinner() {
        if(!this.isTeamGame()) {
            if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if(game.getPlayers().size() == 1) {
                    game.getGameStateManager().setGameState(GameState.ENDING);

                    for (UUID players : game.getPlayers()) {

                        OfflinePlayer allPlayers = Bukkit.getOfflinePlayer(players);

                        Bukkit.broadcastMessage(prefix + mColor + "Congratulations to " + allPlayers.getName() + " for winning this game!");

                        if(game.isDatabaseActive()) {
                            game.getDatabaseManager().addWins(allPlayers, 1);
                        }

                        if(allPlayers.isOnline()) {
                            Player player = Bukkit.getPlayer(players);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().spawn(player.getLocation(), Firework.class);
                                }
                            }.runTaskTimer(game, 0, 20);
                        }
                    }

                    Bukkit.broadcastMessage(prefix + ChatColor.RED + "The server restarts in 1 minute!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }.runTaskLater(game, 20 * 60) ;
                }
            }
        } else {
            if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if(game.getTeamManager().getTeams().size() == 1) {
                    game.getGameStateManager().setGameState(GameState.ENDING);

                    List<String> winnerNames = new ArrayList<>();

                    for (UUID players : game.getPlayers()) {

                        OfflinePlayer allPlayers = Bukkit.getOfflinePlayer(players);

                        winnerNames.add(allPlayers.getName());

                        if(game.isDatabaseActive()) {
                            game.getDatabaseManager().addWins(allPlayers, 1);
                        }

                        if(allPlayers.isOnline()) {
                            Player player = Bukkit.getPlayer(players);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().spawn(player.getLocation(), Firework.class);
                                }
                            }.runTaskTimer(game, 0, 20);
                        }
                    }

                    String[] stringArray = winnerNames.toArray(new String[winnerNames.size()]);

                    Bukkit.broadcastMessage(prefix + mColor + "Congratulations to " + Arrays.toString(stringArray) + " for winning this game!");

                    Bukkit.broadcastMessage(prefix + ChatColor.RED + "The server restarts in 1 minute!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }.runTaskLater(game, 20 * 60);
                }
            }
        }
    }

    public void playSound() {
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.playSound(allPlayers.getLocation(), Sound.ORB_PICKUP, 1, 1);
        }
    }

    public void executeFinalHeal() {
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                allPlayers.setHealth(20);
                allPlayers.setFoodLevel(20);
            }
        }
        this.playSound();
        Bukkit.broadcastMessage(prefix + mColor + "All players have been healed!");
    }

    public void endGracePeriod() {
        game.setInGrace(false);
        this.playSound();
        Bukkit.broadcastMessage(prefix + mColor + "The grace period has ended!");
    }

    public Player getRandomPlayer() {
        int playerNumber = new Random().nextInt(game.getPlayers().size());
        Player player = (Player) Bukkit.getOnlinePlayers().toArray()[playerNumber];

        if(!game.getSpectators().contains(player)) {
            return (Player) Bukkit.getOnlinePlayers().toArray()[playerNumber];
        }
        return null;
    }

    public void setWhitelisted(boolean whitelisted) {
        this.whitelisted = whitelisted;
    }

    public boolean isWhitelisted() {
        return whitelisted;
    }

    public void setTeamGame(boolean teamGame) {
        this.teamGame = teamGame;
    }

    public boolean isTeamGame() {
        return teamGame;
    }
}
