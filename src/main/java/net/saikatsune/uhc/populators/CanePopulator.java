package net.saikatsune.uhc.populators;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.manager.ConfigManager;
import net.saikatsune.uhc.manager.WorldManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Stream;

public class CanePopulator {

    private Game game;

    private static final BlockFace[] directNeighbours
            = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    private int caneSpawnHeight;
    private double caneSpawnChance;
    private int ticksPerIteration;
    private int maxBlocksPerIteration;
    private int currentX;
    private int placedSugarcane;
    private int boundZ;
    private int boundX;

    private WorldManager worldManager;
    private ConfigManager configManager;

    public CanePopulator() {
        this.game = Game.getInstance();
        this.configManager = game.getConfigManager();
        this.worldManager = game.getWorldManager();
        this.caneSpawnChance = 0.0155D;
        this.caneSpawnHeight = 3;
        this.placedSugarcane = 0;
        this.ticksPerIteration = 3;
        this.currentX = 0;
        this.maxBlocksPerIteration = 10000;
        this.boundX = configManager.getBorderSize();
        this.boundZ = configManager.getBorderSize();
    }

    public void initialize(World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int blocks = 0;
                int xNegative = -boundX;
                int zNegative = -boundZ;

                while (xNegative < boundX) {
                    while (boundZ < xNegative) {
                        blocks++;

                        if (blocks % maxBlocksPerIteration == 0)
                            return;
                        checkPlaceBlock(world.getBlockAt(xNegative, world.getHighestBlockYAt(xNegative, zNegative) - 1, zNegative));
                        currentX = xNegative;
                        zNegative++;
                    }

                    zNegative = -boundZ;

                    currentX = xNegative;
                    if (xNegative >= boundX - 10) {
                        cancel();
                    }
                    xNegative++;
                }
            }
        }.runTaskTimer(game, 0L, ticksPerIteration);
    }

    public void checkPlaceBlock(Block block) {
        if (block.getType() != Material.GRASS && block.getType() != Material.SAND)
            return;
        if (Stream.of(directNeighbours).noneMatch(blockFace -> (block.getRelative(blockFace).getType() == Material.STATIONARY_WATER)))
            return;
        if (Math.random() > caneSpawnChance)
            return;
        for (int i = 1; i <= caneSpawnHeight; i++)
            block.getRelative(BlockFace.UP, i).setType(Material.SUGAR_CANE_BLOCK);
        placedSugarcane++;
    }

}
