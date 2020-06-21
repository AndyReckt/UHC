package net.saikatsune.uhc.listener;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.gamestate.states.EndingState;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import net.saikatsune.uhc.gamestate.states.ScatteringState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("DuplicateCondition")
public class ConnectionListener implements Listener {

    private final Game game = Game.getInstance();

    @EventHandler
    public void handlePlayerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage("");

        Player player = event.getPlayer();

        game.getArenaPlayers().remove(player.getUniqueId());

        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            game.getGameManager().resetPlayer(player);

            try {
                player.teleport(game.getLocationManager().getLocation("Spawn-Location"));
            } catch (Exception exception) {
                player.sendMessage(game.getPrefix() + ChatColor.RED + "There is no spawn-location set yet.");
                player.sendMessage(game.getPrefix() + ChatColor.RED + "Use /setup to set it. It is needed to work properly.");
            }

            game.getInventoryHandler().handleLobbyInventory(player);

            game.getTeamNumber().putIfAbsent(player.getUniqueId(), -1);

            game.getGameManager().setPlayerState(player, PlayerState.PLAYER);

            for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                    allPlayers.showPlayer(player);
                    player.showPlayer(allPlayers);
                }
            }
        } else if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(!game.getLoggedPlayers().contains(player.getUniqueId())) {
                game.getGameManager().resetPlayer(player);

                if(player.hasPermission("uhc.host")) {
                    game.getInventoryHandler().handleStaffInventory(player);
                }

                game.getGameManager().setPlayerState(player, PlayerState.SPECTATOR);

                for (Player allSpectators : game.getSpectators()) {
                    allSpectators.hidePlayer(player);
                    player.hidePlayer(allSpectators);
                }
            } else {
                /*
                if(game.getGameManager().isTeamGame()) {
                    if(game.getTeamNumber().get(player.getUniqueId()) != -1) {
                        TeamHandler teamHandler = game.getTeamManager().getTeams().get(game.getTeamNumber().get(player.getUniqueId()));
                        if(game.getTeamManager().getTeams().containsValue(teamHandler)) {
                            game.getTeamManager().addPlayerToTeam(game.getTeamNumber().get(player.getUniqueId()), player.getUniqueId());
                        } else {
                            game.getTeamManager().createTeam(player.getUniqueId());
                        }
                    }
                }
                 */

                /*
                for (UUID allPlayers : game.getPlayers()) {
                    OfflinePlayer players = Bukkit.getPlayer(allPlayers);

                    //players.showPlayer(player);

                    player.showPlayer(playerOnline);
                }
                 */

                game.getGameManager().removeCombatVillager(game.getCombatVillagerUUID().get(player.getUniqueId()));

                game.getGameManager().setPlayerState(player, PlayerState.PLAYER);
            }
        }

        if(game.isDatabaseActive()) {
            game.getDatabaseManager().registerPlayer(player);
        }

        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.SLOW);

        game.getLoggedOutPlayers().remove(player.getUniqueId());

        game.getPlayerKills().putIfAbsent(player.getUniqueId(), 0);
        game.getLogoutTimer().putIfAbsent(player.getUniqueId(), game.getRelogTimeInMinutes() * 60);
    }

    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage("");

        Player player = event.getPlayer();

        //game.getPlayers().remove(player.getUniqueId());

        /*
        if(game.getGameManager().isTeamGame()) {
            if(game.getTeamNumber().get(player.getUniqueId()) != -1) {
                game.getTeamManager().removePlayerFromTeam(game.getTeamNumber().get(player.getUniqueId()), player.getUniqueId());
            }
        }
         */

        game.getScatterTask().getPlayersToScatter().remove(player.getUniqueId());

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(!game.getSpectators().contains(player)) {
                game.getGameManager().spawnCombatVillager(player);
                game.registerPlayerDeath(player);
            }

            if(game.getPlayers().contains(player.getUniqueId())) {
                game.getLoggedOutPlayers().add(player.getUniqueId());
            }
        } else if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState || game.getGameStateManager().getCurrentGameState() instanceof ScatteringState) {
            game.getPlayers().remove(player.getUniqueId());

            if(game.getGameManager().isTeamGame()) {
                if(game.getTeamNumber().get(player.getUniqueId()) != -1) {
                    game.getTeamManager().removePlayerFromTeam(game.getTeamNumber().get(player.getUniqueId()), player.getUniqueId());
                }
            }
        }

        game.getArenaPlayers().remove(player.getUniqueId());
        game.getSpectators().remove(player);
        game.getGameManager().checkWinner();
    }

    @EventHandler
    public void handlePlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            if(game.getGameManager().isWhitelisted()) {
                if(!game.getWhitelisted().contains(player.getUniqueId())) {
                    if(!player.hasPermission("uhc.host") || !player.hasPermission("uhc.donator")) {
                        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The game is currently whitelisted!");
                    }
                }
            }
        } else if(game.getGameStateManager().getCurrentGameState() instanceof ScatteringState) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The scatter is currently happening. Try again later!");
        } else if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            if(game.getGameManager().isWhitelisted()) {
                if(!game.getWhitelisted().contains(player.getUniqueId())) {
                    if(!game.getLoggedPlayers().contains(player.getUniqueId())) {
                        if(!player.hasPermission("uhc.host")) {
                            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The game has already started!");
                        }
                    }
                }
            }
        } else if(game.getGameStateManager().getCurrentGameState() instanceof EndingState) {
            if(!player.hasPermission("uhc.host")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cThe game has already ended!");
            }
        }
    }
}
