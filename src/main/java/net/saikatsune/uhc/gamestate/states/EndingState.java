package net.saikatsune.uhc.gamestate.states;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class EndingState extends GameState {

    private final Game game = Game.getInstance();

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                game.getTimeTask().cancelTask();
            }
        }.runTaskLater(game, 20);
    }

    public void stop() {

    }
}
