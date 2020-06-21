package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LatescatterCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("latescatter")) {
            Player player = (Player) sender;
            if(args.length == 0) {
                if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                    if(game.getSpectators().contains(player)) {
                        if(game.isInGrace()) {
                            if(!game.getDeadPlayersByUUID().contains(player.getUniqueId())) {
                                game.getGameManager().resetPlayer(player);
                                game.getGameManager().setPlayerState(player, PlayerState.PLAYER);

                                Random randomLocation = new Random();

                                int x = randomLocation.nextInt(game.getConfigManager().getBorderSize() - 1);
                                int z = randomLocation.nextInt(game.getConfigManager().getBorderSize() - 1);
                                int y = Bukkit.getWorld("uhc_world").getHighestBlockYAt(x, z);

                                Location location = new Location(Bukkit.getWorld("uhc_world"), x, y ,z);

                                game.getGameManager().setScatterLocation(player, location);

                                player.teleport(game.getScatterLocation().get(player));

                                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, game.getConfigManager().getStarterFood()));

                                if(game.getGameManager().isTeamGame()) {
                                    game.getTeamManager().createTeam(player.getUniqueId());
                                }

                                game.getLoggedPlayers().add(player.getUniqueId());
                                game.getWhitelisted().add(player.getUniqueId());

                                if(Scenarios.GoneFishing.isEnabled()) {
                                    player.getInventory().addItem(new ItemStack(Material.ANVIL, 20));

                                    player.setLevel(30000);

                                    ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                                    fishingRod.addUnsafeEnchantment(Enchantment.LUCK, 250);
                                    fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);
                                    fishingRod.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
                                    player.getInventory().addItem(fishingRod);
                                }

                                if(Scenarios.InfiniteEnchant.isEnabled()) {
                                    player.setLevel(30000);
                                    player.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                                    player.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                    player.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                    player.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                }

                                if(Scenarios.BestPVE.isEnabled()) {
                                    game.getBestPvePlayers().add(player.getUniqueId());
                                    player.sendMessage(prefix + ChatColor.GREEN + "You have been added to the BestPVE list.");
                                }

                                Bukkit.broadcastMessage(prefix + mColor + player.getName() + sColor + " has been scattered.");
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "You have already died this game.");
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.RED + "You can no longer late-scatter yourself.");
                        }
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "You are already in-game.");
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "There is currently no game running.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /latescatter");
            }
        }
        return false;
    }
}
