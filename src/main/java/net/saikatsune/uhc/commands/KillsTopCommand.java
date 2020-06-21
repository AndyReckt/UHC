package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class KillsTopCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    @Override
    public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("killstop")) {
            if(!(game.getGameStateManager().getCurrentGameState() instanceof LobbyState)) {
                player.sendMessage("§8§m----------------------------");
                player.sendMessage(mColor + "Top 10 Kills: ");

                player.sendMessage("");

                Map<UUID, Integer> unsortedkills = new HashMap<>();
                for(UUID allPlayers : game.getPlayers()) {
                    unsortedkills.put(allPlayers, game.getPlayerKills().get(allPlayers));
                }
                Map<UUID, Integer> kills = sortByValue(unsortedkills);
                int x = 1;
                for(Object object : kills.keySet()) {
                    if(x != 11) {
                        UUID uuid = (UUID) object;
                        if(kills.get(uuid) != 0) {
                            player.sendMessage(sColor + "- " + mColor + Bukkit.getOfflinePlayer(uuid).getName() + sColor + ": " + kills.get(uuid) + " Kill(s)");
                        }
                        x++;
                    } else {
                        break;
                    }
                }
                player.sendMessage("§8§m----------------------------");
            } else {
                player.sendMessage(prefix + ChatColor.RED + "There is currently no game running.");
            }
        }
        return false;
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
