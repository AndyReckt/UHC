package net.saikatsune.uhc.gamestate.states;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyState extends GameState {

    private final Game game = Game.getInstance();

    public void start() {

    }

    public void stop() {
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                allPlayers.getInventory().clear();
            }
        }
    }
}
