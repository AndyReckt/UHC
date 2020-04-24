package net.saikatsune.uhc.board;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import net.saikatsune.uhc.gamestate.states.ScatteringState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UHCBoardProvider implements BoardProvider {

    private Game game;

    public UHCBoardProvider(Game game) {
        this.game = game;
    }

    @Override
    public String getTitle(Player player) {
        return game.getScoreboardManager().getConfig().getString("SCOREBOARDS.TITLE").replace("&", "§");
    }

    @Override
    public List<String> getBoardLines(Player player) {
        List<String> lines = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        FileConfiguration config = game.getScoreboardManager().getConfig();

        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            if(!game.getGameManager().isTeamGame()) {
                for (int i = 0; i < config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES").size(); i++) {
                    List<String> path = config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES");

                    lines.add(path.get(i).replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", String.valueOf(game.getSpectators().size()))).
                            replace("%gameHost%", game.getGameHost()).
                            replace("%scenarios%", "Gamemodes:")
                            .replace("&", "§"));

                    if(config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES").get(i).contains("%scenarios%")) {
                        for (int y = 0; y < game.getScenariosInList().size(); y++) {
                            if (y <= 2) {
                                lines.add(ChatColor.WHITE + " - " + game.getScenariosInList().get(y));
                            } else {
                                lines.removeIf(line -> line.contains("more..."));

                                int scenariosMore = game.getScenariosInList().size() - 3;

                                lines.add(ChatColor.WHITE + " - +" + scenariosMore + " more...");
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES").size(); i++) {
                    List<String> path = config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES");

                    lines.add(path.get(i).replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", String.valueOf(game.getSpectators().size()))).
                            replace("%gameHost%", game.getGameHost()).
                            replace("%scenarios%", "Gamemodes:")
                            .replace("&", "§"));

                    if(config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES").get(i).contains("%scenarios%")) {
                        for (int y = 0; y < game.getScenariosInList().size(); y++) {
                            if (y <= 2) {
                                lines.add(ChatColor.WHITE + " - " + game.getScenariosInList().get(y));
                            } else {
                                lines.removeIf(line -> line.contains("more..."));

                                int scenariosMore = game.getScenariosInList().size() - 3;

                                lines.add(ChatColor.WHITE + " - +" + scenariosMore + " more...");
                            }
                        }
                    }
                }
            }

            /*
            lines.add(ChatColor.RED + "Game Host: ");
                    lines.add(ChatColor.WHITE + " - " + game.getGameHost());
                    lines.add("");
                    lines.add(ChatColor.RED + "Teams: " + ChatColor.WHITE + game.getTeamSizeInString());
                    lines.add("");
                    lines.add(ChatColor.RED + "Gamemodes: ");

                    lines.add("");
                    lines.add(ChatColor.RED + "Players: " + ChatColor.WHITE + game.getPlayers().size());
                    lines.add("");
                    lines.add(ChatColor.GRAY + "www.daredevil.rip");
             */

            /*
            if(!game.getGameManager().isTeamGame()) {
                for (String string : config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost()).
                            replace("%scenarios%", "");
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            } else {
                for (String string : config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%teamsAlive%", String.valueOf(game.getTeamManager().getTeams().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            }
             */
        } else if(game.getGameStateManager().getCurrentGameState() instanceof ScatteringState) {
            lines.clear();
            if(!game.getGameManager().isTeamGame()) {
                for (String string : config.getStringList("SCOREBOARDS.SOLO.SCATTERING.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%scattering%", String.valueOf(game.getScatterTask().getPlayersToScatter().size())).
                            replace("%scattered%", String.valueOf(game.getScatterTask().getPlayersScattered().size())).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            } else {
                for (String string : config.getStringList("SCOREBOARDS.TEAMS.SCATTERING.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%scattering%", String.valueOf(game.getScatterTask().getPlayersToScatter().size())).
                            replace("%scattered%", String.valueOf(game.getScatterTask().getPlayersScattered().size())).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            }
        } else {
            lines.clear();
            if(!game.getGameManager().isTeamGame()) {
                for (String string : config.getStringList("SCOREBOARDS.SOLO.INGAME.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            } else {
                for (String string : config.getStringList("SCOREBOARDS.TEAMS.INGAME.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%teamsAlive%", String.valueOf(game.getTeamManager().getTeams().size())).
                            replace("%teamKills%", String.valueOf(game.getTeamManager().getTeamKills(player))).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            }
        }

        return lines;
    }
}
