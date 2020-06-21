package net.saikatsune.uhc.manager;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {

    private final Game game = Game.getInstance();

    private final File scoreboardsFile = new File(game.getDataFolder(), "scoreboards.yml");
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(scoreboardsFile);

    public File getScoreboardsFile() {
        return scoreboardsFile;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
