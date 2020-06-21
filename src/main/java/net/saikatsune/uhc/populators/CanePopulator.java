package net.saikatsune.uhc.populators;

import net.saikatsune.uhc.Game;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class CanePopulator extends BlockPopulator {

    private final int canePatchChance;

    private final Material sugarCane;

    private final BlockFace[] blockFaces = new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

    public CanePopulator() {
        Game game = Game.getInstance();
        this.canePatchChance = game.getConfig().getInt("POPULATORS.SUGARCANE.PERCENTAGE");
        this.sugarCane = Material.SUGAR_CANE_BLOCK;
    }

    private Block getHighestBlock(Chunk chunk, int x, int z) {
        Block block = null;
        for (int i = 127; i >= 0; ) {
            if ((block = chunk.getBlock(x, i, z)).getTypeId() != 0)
                return block;
            i--;
        }
        return block;
    }

    private void createCane(Block block, Random rand) {
        boolean create = false;
        for (BlockFace face : blockFaces) {
            if (block.getRelative(face).getType().name().toLowerCase().contains("water"))
                create = true;
        }
        if (!create)
            return;
        for (int i = 1; i < rand.nextInt(4) + 3; ) {
            block.getRelative(0, i, 0).setType(sugarCane);
            i++;
        }
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (random.nextInt(100) < canePatchChance)
            for (int i = 0; i < 16; i++) {
                Block block;
                if (random.nextBoolean()) {
                    block = getHighestBlock(chunk, random.nextInt(16), i);
                } else {
                    block = getHighestBlock(chunk, i, random.nextInt(16));
                }
                if (block.getType() == Material.GRASS || block.getType() == Material.SAND)
                    createCane(block, random);
            }
    }
}
