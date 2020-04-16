package net.saikatsune.uhc.manager;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.handler.FileHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class WorldManager {

    private Game game = Game.getInstance();

    private String prefix = game.getPrefix();

    private String mColor = game.getmColor();
    private String sColor = game.getsColor();

    private FileHandler fileHandler = game.getFileHandler();

    public void createWorld(String worldName, World.Environment environment, WorldType worldType) {
        Bukkit.broadcastMessage(prefix + sColor + "Started creating the world " + mColor + worldName + sColor + "...");
        World world = Bukkit.createWorld(new WorldCreator(worldName).environment(environment).type(worldType));
        world.setDifficulty(Difficulty.EASY);
        world.setTime(0);
        world.setThundering(false);
        world.setGameRuleValue("naturalRegeneration", "false");
        Bukkit.broadcastMessage(prefix + sColor + "Finished creating the world " + mColor + worldName + sColor + "!");
    }

    public void loadWorld(String worldName, int worldRadius, int loadingSpeed) {
        Bukkit.broadcastMessage(prefix + sColor + "Started clearing the world " + mColor + worldName + sColor + "...");
        this.clearCenter(Bukkit.getWorld(worldName));

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(prefix + sColor + "Started loading the world " + mColor + worldName + sColor + "...");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " set " + worldRadius + " " + worldRadius + " 0 0");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " fill " + loadingSpeed);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " fill confirm");
            }
        }.runTaskLater(game, 20 * 20);
    }

    // COMPLETELY TAKEN FROM BLADIAN'S UHC
    // LINKS:
    // GITHUB: https://github.com/BladianYT/UHC
    // TWITTER: https://twitter.com/BladianMC/

    private void clearCenter(final World world) {
        final Queue<Location> locationQueue = new ArrayDeque<>();

        final Location max = new Location(world, 125, 160, 125);
        final Location min = new Location(world, -125, 45, -125);

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    final Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
                            || block.getType() == Material.VINE || block.getType() == Material.SNOW || block.getType() == Material.DOUBLE_PLANT
                            || block.getType() == Material.YELLOW_FLOWER || block.getType() == Material.RED_MUSHROOM || block.getType() == Material.BROWN_MUSHROOM) {
                        locationQueue.add(block.getLocation());
                        if (x == 125 && z == 125 || x == -125 || z == -125) {
                            //loop(block, locationQueue);
                            //Need to try and find a solution for looping
                        }
                    }
                }
            }
        }

        final int blocks = locationQueue.size();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int y = 0; y < 150; y++) {
                    if (!locationQueue.isEmpty()) {
                        Location location = locationQueue.poll();
                        location.getBlock().setType(Material.AIR);
                    } else {
                        this.cancel();
                        if (y == 149) {
                            Bukkit.broadcastMessage(prefix + sColor + "Finished clearing " + mColor + world.getName() + " " + sColor +
                                    "by changing " + mColor + blocks + sColor + " blocks.");
                            Bukkit.broadcastMessage(prefix + sColor + "Started patching " + mColor + world.getName() + sColor + ".");
                            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                                    Block bl = Bukkit.getWorld("uhc_world").getHighestBlockAt(x, z);
                                    Block blk = bl.getLocation().add(0, -1, 0).getBlock();
                                    if (blk.getType() == Material.DIRT) {
                                        locationQueue.add(blk.getLocation());
                                    }
                                }
                            }
                            final int blocks2 = locationQueue.size();
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    for (int y = 0; y < 150; y++) {
                                        if (!locationQueue.isEmpty()) {
                                            Location location = locationQueue.poll();
                                            location.getBlock().setType(Material.GRASS);
                                        } else {
                                            this.cancel();
                                            if (y == 149) {
                                                this.cancel();
                                                Bukkit.broadcastMessage(prefix + sColor + "Finished patching " + mColor + world.getName() + sColor + " by changing " +
                                                       mColor + blocks2 + sColor + " blocks.");
                                            }
                                        }
                                    }
                                }
                            }.runTaskTimer(game, 0L, 1L);
                        }
                    }
                }
            }
        }.runTaskTimer(game, 0L, 1L);
    }

    public void prepareSpawn() {
        final int[] i = {0};
        final Queue<Location> locationQueue = new ArrayDeque<>();
        final Queue<Material> materialQueue = new ArrayDeque<>();
        final World world = Bukkit.getWorld("uhc_world");
        final Location max = new Location(world, 125, 160, 125);
        final Location min = new Location(world, -125, 50, -125);
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    final Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    if (block.getType() == Material.GRASS) {
                        locationQueue.add(block.getLocation());
                        materialQueue.add(Material.STAINED_CLAY);
                    } else if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
                            || block.getType() == Material.VINE || block.getType() == Material.SNOW || block.getType() == Material.DOUBLE_PLANT
                            || block.getType() == Material.YELLOW_FLOWER || block.getType() == Material.RED_MUSHROOM || block.getType() == Material.BROWN_MUSHROOM) {
                        locationQueue.add(block.getLocation());
                        materialQueue.add(Material.AIR);
                    }
                }
            }
        }

        final int blocks = locationQueue.size();
        new BukkitRunnable() {

            @Override
            public void run() {
                for (int x = 0; x < 150; x++) {
                    if (!locationQueue.isEmpty()) {
                        Location location = locationQueue.poll();
                        Material material = materialQueue.poll();
                        if (material == Material.STAINED_CLAY) {
                            int rand = (int) ((Math.random() * 2) + 1);
                            int data;
                            if (rand == 1) {
                                data = 1;
                            } else {
                                data = 4;
                            }
                            location.getBlock().setType(Material.STAINED_CLAY);
                            location.getBlock().setData((byte) data);
                        } else {
                            location.getBlock().setType(Material.AIR);
                        }
                    } else {
                        this.cancel();
                        if (x == 149) {
                            Bukkit.broadcastMessage(prefix + sColor + "Finished clearing " + mColor + world.getName() + sColor + " by " +
                                    "changing " + mColor + blocks + sColor + " blocks.");
                        }
                    }
                }
            }
        }.runTaskTimer(game, 0L, 1L);
    }

    private void loop(Block block1, Queue<Location> locations) {
        for (BlockFace blockface : BlockFace.values()) {
            if (block1.getRelative(blockface).getType().equals(Material.LOG) || block1.getRelative(blockface).getType().equals(Material.LOG_2)
                    || block1.getRelative(blockface).getType() == Material.LEAVES || block1.getRelative(blockface).getType() == Material.LEAVES_2) {
                Block block = block1.getRelative(blockface);
                locations.add(block.getLocation());
                loop(block, locations);
            }
        }
    }

    public void shrinkBorder(String worldName, int size) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " set " + size + " " + size + " 0 0");
    }

    public void createTotalShrink() {
        if (game.getConfigManager().getBorderSize() == 3500) {
            game.getConfigManager().setBorderSize(3000);
            game.getWorldManager().shrinkBorder("uhc_world", 3000);
            game.getWorldManager().createBorderLayer("uhc_world",3000, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 3000);
        } else if (game.getConfigManager().getBorderSize() == 3000) {
            game.getConfigManager().setBorderSize(2500);
            game.getWorldManager().shrinkBorder("uhc_world", 2500);
            game.getWorldManager().createBorderLayer("uhc_world",2500, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 2500);
        } else if (game.getConfigManager().getBorderSize() == 2500) {
            game.getConfigManager().setBorderSize(2000);
            game.getWorldManager().shrinkBorder("uhc_world", 2000);
            game.getWorldManager().createBorderLayer("uhc_world",2000, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 2000);
        } else if (game.getConfigManager().getBorderSize() == 2000) {
            game.getConfigManager().setBorderSize(1500);
            game.getWorldManager().shrinkBorder("uhc_world", 1500);
            game.getWorldManager().createBorderLayer("uhc_world",1500, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 1500);
        } else if (game.getConfigManager().getBorderSize() == 1500) {
            game.getConfigManager().setBorderSize(1000);
            game.getWorldManager().shrinkBorder("uhc_world", 1000);
            game.getWorldManager().createBorderLayer("uhc_world",1000, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 1000);
        } else if (game.getConfigManager().getBorderSize() == 1000) {
            game.getConfigManager().setBorderSize(500);
            game.getWorldManager().shrinkBorder("uhc_world", 500);
            game.getWorldManager().createBorderLayer("uhc_world",500, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 500);
        } else if (game.getConfigManager().getBorderSize() == 500) {
            game.getConfigManager().setBorderSize(100);
            game.getWorldManager().shrinkBorder("uhc_world", 100);
            game.getWorldManager().createBorderLayer("uhc_world",100, 4, null);

            if(game.getConfigManager().isNether()) {
                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                        if(allPlayers.getWorld().getName().equalsIgnoreCase("uhc_nether")) {

                            Random randomLocation = new Random();

                            int x = randomLocation.nextInt(game.getConfigManager().getBorderSize() - 1);
                            int z = randomLocation.nextInt(game.getConfigManager().getBorderSize() - 1);
                            int y = Bukkit.getWorld("uhc_world").getHighestBlockYAt(x, z);

                            Location location = new Location(Bukkit.getWorld("uhc_world"), x, y ,z);

                            game.getGameManager().setScatterLocation(allPlayers, location);

                            allPlayers.teleport(game.getScatterLocation().get(allPlayers));
                        }
                    }
                }
            }

            game.getConfigManager().setNether(false);

            game.getWorldManager().shrinkBorder("uhc_nether", 100);
        } else if(game.getConfigManager().getBorderSize() == 100) {
            game.getConfigManager().setBorderSize(50);
            game.getWorldManager().shrinkBorder("uhc_world", 50);
            game.getWorldManager().createBorderLayer("uhc_world",50, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 50);
        } else if(game.getConfigManager().getBorderSize() == 50) {
            game.getConfigManager().setBorderSize(25);
            game.getWorldManager().shrinkBorder("uhc_world", 25);
            game.getWorldManager().createBorderLayer("uhc_world",25, 4, null);

            game.getWorldManager().shrinkBorder("uhc_nether", 25);
        }
        game.getGameManager().playSound();
        Bukkit.broadcastMessage(prefix + sColor + "The border has shrunk to " + mColor + game.getConfigManager().getBorderSize() + "x" +
                game.getConfigManager().getBorderSize() + sColor + " blocks!");
        if(game.getConfigManager().getBorderSize() > 25) {
            Bukkit.broadcastMessage(prefix + sColor + "The border is going to shrink to " + game.getTimeTask().getNextBorder() + "x" +
                    game.getTimeTask().getNextBorder() + " blocks in 5 minutes!");
        }
    }

    public void createBorderLayer(String borderWorld, int radius, int amount, Consumer<String> done) {
        World world = Bukkit.getWorld(borderWorld);
        if (world == null) return;
        LinkedList<Location> locations = new LinkedList<>();

        for (int i = 0; i < amount; i++) {
            for (int z = -radius; z <= radius; z++) {
                Location location = new Location(world, radius, world.getHighestBlockYAt(radius, z) + i, z);
                locations.add(location);
            }
            for (int z = -radius; z <= radius; z++) {
                Location location = new Location(world, -radius, world.getHighestBlockYAt(-radius, z) + i, z);
                locations.add(location);
            }
            for (int x = -radius; x <= radius; x++) {
                Location location = new Location(world, x, world.getHighestBlockYAt(x, radius) + i, radius);
                locations.add(location);
            }
            for (int x = -radius; x <= radius; x++) {
                Location location = new Location(world, x, world.getHighestBlockYAt(x, -radius) + i, -radius);
                locations.add(location);
            }
        }

        new BukkitRunnable() {

            private int max = 50;
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    if (locations.isEmpty()) {
                        if (done != null) done.accept("done");
                        this.cancel();
                        break;
                    }
                    locations.remove().getBlock().setType(Material.BEDROCK);
                }
            }
        }.runTaskTimer(game, 0, 1);
    }

    private void unloadWorld(World worldName) {
        if(worldName != null) {
            Bukkit.unloadWorld(worldName, false);
        }
    }

    public void deleteWorld(String worldName) {
        Bukkit.broadcastMessage(prefix + sColor + "Started deleting the world " + mColor + worldName + sColor + "...");
        this.unloadWorld(Bukkit.getWorld(worldName));
        fileHandler.deleteFiles(new File(Bukkit.getWorldContainer(), worldName));
        Bukkit.broadcastMessage(prefix + sColor + "Finished deleting the world " + mColor + worldName + sColor + "!");
    }

}
