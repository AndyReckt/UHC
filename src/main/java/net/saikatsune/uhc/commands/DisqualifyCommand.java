package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.handler.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class DisqualifyCommand implements CommandExecutor, Listener {

    private final Game game = Game.getInstance();

    private final HashMap<UUID, UUID> disqualifyHash = new HashMap<>();

    private final String prefix = game.getPrefix();
    private final String mColor = game.getmColor();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("disqualify")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;

                if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                    if(game.getGameHost().equalsIgnoreCase(player.getName())) {
                        game.getInventoryHandler().handleDisqualifyInventory(player);
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "You have to be the host of the game to execute this command.");
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "There is currently no game running.");
                }
            }
        }
        return false;
    }

    @EventHandler
    public void handleInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getClickedInventory() != null) {
            if(event.getCurrentItem() != null) {
                if((event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) || event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                    event.setCancelled(true);
                }

                if(event.getClickedInventory().getName().contains(mColor + "Disqualify a player.")) {
                    if(event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                        event.setCancelled(true);

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(event.getCurrentItem().getItemMeta().getDisplayName());

                        disqualifyHash.put(player.getUniqueId(), offlinePlayer.getUniqueId());

                        player.closeInventory();

                        Inventory inventory = Bukkit.createInventory(null, 9, mColor + "Are you sure?");

                        ItemStack playerStack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                        SkullMeta playersMeta = (SkullMeta) playerStack.getItemMeta();
                        playersMeta.setOwner(offlinePlayer.getName());
                        playersMeta.setDisplayName(offlinePlayer.getName());
                        playerStack.setItemMeta(playersMeta);

                        inventory.setItem(4, playerStack);
                        inventory.setItem(2, new ItemHandler(Material.CACTUS).setDisplayName(ChatColor.GREEN + "§lYES").build());
                        inventory.setItem(6, new ItemHandler(Material.REDSTONE_BLOCK).setDisplayName(ChatColor.RED + "§lNO").build());

                        game.getInventoryHandler().fillEmptySlots(inventory);

                        player.openInventory(inventory);
                    }
                } else if(event.getClickedInventory().getName().contains(mColor + "Are you sure?")) {
                    if(event.getCurrentItem().getType() == Material.CACTUS) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.GREEN + "§lYES")) {
                            event.setCancelled(true);

                            player.closeInventory();

                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(disqualifyHash.get(player.getUniqueId()));

                            disqualifyHash.remove(disqualifyHash.get(player.getUniqueId()));
                            disqualifyHash.remove(player.getUniqueId());

                            game.getLoggedOutPlayers().remove(offlinePlayer.getUniqueId());

                            game.getGameManager().disqualifyPlayer(offlinePlayer);

                            try {
                                game.getGameManager().dropPlayerDeathInventory(offlinePlayer.getUniqueId());
                            } catch (Exception ignored) {}
                        }
                    } else if(event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.RED + "§lNO")) {
                            event.setCancelled(true);

                            player.closeInventory();

                            disqualifyHash.remove(disqualifyHash.get(player.getUniqueId()));
                            disqualifyHash.remove(player.getUniqueId());
                        }
                    } else if(event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
