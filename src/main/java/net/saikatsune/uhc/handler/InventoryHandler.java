package net.saikatsune.uhc.handler;

import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InventoryHandler {

    private final Game game = Game.getInstance();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    private final Inventory leaderboardsInventory = Bukkit.createInventory(null, 9*5, mColor + "Leaderboards");

    public void fillEmptySlots(Inventory inventory) {
        for(int slot = 0; slot < inventory.getSize(); slot++) {
            if(inventory.getItem(slot) == null) {
                inventory.setItem(slot, new ItemStack(Material.STAINED_GLASS_PANE));
            }
        }
    }

    public void handleSetupInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*1, mColor + "Server Setup");

        inventory.setItem(2, new ItemHandler(Material.FLINT_AND_STEEL).setDisplayName(mColor + "Game Setup").build());
        inventory.setItem(6, new ItemHandler(Material.DIAMOND_PICKAXE).setDisplayName(mColor + "Lobby Setup").build());

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleLobbySetupInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*1, mColor + "Lobby Setup");

        inventory.setItem(4, new ItemHandler(Material.ANVIL).setDisplayName(mColor + "Spawn-Location").build());

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleGameSetupInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*1, mColor + "Game Setup");

        inventory.setItem(1, new ItemHandler(Material.GRASS).setDisplayName(mColor + "World Editor").build());
        inventory.setItem(3, new ItemHandler(Material.BLAZE_ROD).setDisplayName(mColor + "Scenarios Editor").build());
        inventory.setItem(5, new ItemHandler(Material.BOOK_AND_QUILL).setDisplayName(mColor + "Config Editor").build());
        inventory.setItem(7, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "Border Editor").build());

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleWorldEditorInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*3, mColor + "World Editor");

        inventory.setItem(2, new ItemHandler(Material.GRASS).setDisplayName(mColor + "Create World").build());
        inventory.setItem(4, new ItemHandler(Material.GRASS).setDisplayName(mColor + "Load World").build());
        inventory.setItem(6, new ItemHandler(Material.GRASS).setDisplayName(mColor + "Delete World").build());

        inventory.setItem(20, new ItemHandler(Material.NETHERRACK).setDisplayName(mColor + "Create Nether").build());
        inventory.setItem(22, new ItemHandler(Material.NETHERRACK).setDisplayName(mColor + "Load Nether").build());
        inventory.setItem(24, new ItemHandler(Material.NETHERRACK).setDisplayName(mColor + "Delete Nether").build());

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleBorderEditorInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*1, mColor + "Border Editor");

        inventory.setItem(1, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "3500 Border").build());
        inventory.setItem(2, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "3000 Border").build());
        inventory.setItem(3, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "2500 Border").build());
        inventory.setItem(4, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "2000 Border").build());
        inventory.setItem(5, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "1500 Border").build());
        inventory.setItem(6, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "1000 Border").build());
        inventory.setItem(7, new ItemHandler(Material.BEDROCK).setDisplayName(mColor + "500 Border").build());

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleConfigEditorInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*3, mColor + "Config Editor");

        if(game.getConfigManager().isNether()) {
            inventory.setItem(1, new ItemHandler(Material.OBSIDIAN).setDisplayName(mColor + "Nether §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(1, new ItemHandler(Material.OBSIDIAN).setDisplayName(mColor + "Nether §7§l➡ §cFALSE").build());
        }
        if(game.getConfigManager().isShears()) {
            inventory.setItem(2, new ItemHandler(Material.SHEARS).setDisplayName(mColor + "Shears §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(2, new ItemHandler(Material.SHEARS).setDisplayName(mColor + "Shears §7§l➡ §cFALSE").build());
        }
        if(game.getConfigManager().isSpeed1()) {
            inventory.setItem(3, new ItemHandler(Material.SUGAR).setDisplayName(mColor + "Speed I §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(3, new ItemHandler(Material.SUGAR).setDisplayName(mColor + "Speed I §7§l➡ §cFALSE").build());
        }
        if(game.getConfigManager().isSpeed2()) {
            inventory.setItem(4, new ItemHandler(Material.SUGAR).setDisplayName(mColor + "Speed II §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(4, new ItemHandler(Material.SUGAR).setDisplayName(mColor + "Speed II §7§l➡ §cFALSE").build());
        }
        if(game.getConfigManager().isStrength1()) {
            inventory.setItem(5, new ItemHandler(Material.BLAZE_POWDER).setDisplayName(mColor + "Strength I §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(5, new ItemHandler(Material.BLAZE_POWDER).setDisplayName(mColor + "Strength I §7§l➡ §cFALSE").build());
        }
        if(game.getConfigManager().isStrength2()) {
            inventory.setItem(6, new ItemHandler(Material.BLAZE_POWDER).setDisplayName(mColor + "Strength II §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(6, new ItemHandler(Material.BLAZE_POWDER).setDisplayName(mColor + "Strength II §7§l➡ §cFALSE").build());
        }
        if(game.getConfigManager().isEnderpearlDamage()) {
            inventory.setItem(7, new ItemHandler(Material.ENDER_PEARL).setDisplayName(mColor + "Enderpearl Damage §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(7, new ItemHandler(Material.ENDER_PEARL).setDisplayName(mColor + "Enderpearl Damage §7§l➡ §cFALSE").build());
        }
        if(game.isDatabaseActive()) {
            inventory.setItem(22, new ItemHandler(Material.NETHER_STAR).setDisplayName(mColor + "Stats §7§l➡ §aTRUE").build());
        } else {
            inventory.setItem(22, new ItemHandler(Material.NETHER_STAR).setDisplayName(mColor + "Stats §7§l➡ §cFALSE").build());
        }

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleStaffInventory(Player player) {
        Inventory inventory = player.getInventory();

        inventory.setItem(0, new ItemHandler(Material.WATCH).setDisplayName(mColor + "Players").build());
        inventory.setItem(1, new ItemHandler(Material.BEACON).setDisplayName(mColor + "Random Player").build());

        inventory.setItem(4, new ItemHandler(Material.NETHER_STAR).setDisplayName(mColor + "Teleport to Center").build());

        inventory.setItem(8, new ItemHandler(Material.BOOK).setDisplayName(mColor + "Inspect Inventory").build());
    }

    public void handlePlayersInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 6*9, mColor + "Alive Players");

        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if(game.getPlayers().contains(allPlayers.getUniqueId())) {
                ItemStack playerStack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                SkullMeta playersMeta = (SkullMeta) playerStack.getItemMeta();
                playersMeta.setOwner(allPlayers.getName());
                playersMeta.setDisplayName(allPlayers.getName());
                playerStack.setItemMeta(playersMeta);
                inventory.addItem(playerStack);
            }
        }

        player.openInventory(inventory);
    }

    public void handleStatsInventory(Player player, OfflinePlayer toWatch) {
        Inventory inventory = Bukkit.createInventory(null, 9, sColor + "Stats: " + mColor + toWatch.getName());

        int kills = game.getDatabaseManager().getKills(toWatch);
        int deaths = game.getDatabaseManager().getDeaths(toWatch);
        int totalGames = game.getDatabaseManager().getDeaths(toWatch) + game.getDatabaseManager().getWins(toWatch);

        inventory.setItem(0, new ItemHandler(Material.IRON_SWORD).setDisplayName(mColor + "Kills: " + sColor +
                kills).build());

        inventory.setItem(1, new ItemHandler(Material.FIREBALL).setDisplayName(mColor + "Deaths: " + sColor +
                deaths).build());

        inventory.setItem(2, new ItemHandler(Material.NETHER_STAR).setDisplayName(mColor + "Wins: " + sColor +
                game.getDatabaseManager().getWins(toWatch)).build());

        inventory.setItem(5, new ItemHandler(Material.WATCH).setDisplayName(mColor + "Games Played: " + sColor +
                totalGames).build());

        inventory.setItem(8, new ItemHandler(Material.BEACON).setDisplayName(mColor + "KDR: " + sColor +
                new DecimalFormat("##.##").format(game.getGameManager().getKillDeathRatio(Double.parseDouble(String.valueOf(kills)),
                        Double.parseDouble(String.valueOf(deaths))))).build());

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handleAlertsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*3, sColor + "Alerts: " + mColor + player.getName());

        if(game.getReceivePvpAlerts().contains(player.getUniqueId())) {
            inventory.setItem(11, new ItemHandler(Material.IRON_SWORD).setDisplayName(mColor + "PvP Alerts").setLore(Arrays.asList("§7§m-----------",
                    "§7> §aEnabled", "§7§m-----------")).build());
        } else {
            inventory.setItem(11, new ItemHandler(Material.IRON_SWORD).setDisplayName(mColor + "PvP Alerts").setLore(Arrays.asList("§7§m-----------",
                    "§7> §cDisabled", "§7§m-----------")).build());
        }

        if(game.getReceiveDiamondAlerts().contains(player.getUniqueId())) {
            inventory.setItem(13, new ItemHandler(Material.DIAMOND_ORE).setDisplayName(mColor + "Diamond Alerts").setLore(Arrays.asList("§7§m-----------",
                    "§7> §aEnabled", "§7§m-----------")).build());
        } else {
            inventory.setItem(13, new ItemHandler(Material.DIAMOND_ORE).setDisplayName(mColor + "Diamond Alerts").setLore(Arrays.asList("§7§m-----------",
                    "§7> §cDisabled", "§7§m-----------")).build());
        }

        if(game.getReceiveGoldAlerts().contains(player.getUniqueId())) {
            inventory.setItem(15, new ItemHandler(Material.GOLD_ORE).setDisplayName(mColor + "Gold Alerts").setLore(Arrays.asList("§7§m-----------",
                    "§7> §aEnabled", "§7§m-----------")).build());
        } else {
            inventory.setItem(15, new ItemHandler(Material.GOLD_ORE).setDisplayName(mColor + "Gold Alerts").setLore(Arrays.asList("§7§m-----------",
                    "§7> §cDisabled", "§7§m-----------")).build());
        }

        this.fillEmptySlots(inventory);

        player.openInventory(inventory);
    }

    public void handlePracticeInventory(Player player) {
        PlayerInventory inventory = player.getInventory();

        inventory.setItem(0, new ItemHandler(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
        inventory.setItem(1, new ItemStack(Material.FISHING_ROD));
        inventory.setItem(2, new ItemHandler(Material.BOW).build());

        inventory.setHelmet(new ItemHandler(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        inventory.setChestplate(new ItemHandler(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        inventory.setLeggings(new ItemHandler(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        inventory.setBoots(new ItemHandler(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());

        inventory.setItem(8, new ItemStack(Material.ARROW, 16));
    }

    public void setupLeaderboardsInventory() {
        Inventory inventory = leaderboardsInventory;

        List<String> sorted10KillsPlayers = new ArrayList<>();
        List<String> sorted10DeathPlayers = new ArrayList<>();
        List<String> sorted10WinPlayers = new ArrayList<>();

        inventory.setItem(4, new ItemHandler(Material.BEACON).setDisplayName(mColor + "UHC LEADERBOARDS").build());

        for (int i = 0; i < 10; i++) {
            int place = i + 1;

            if(!game.getDatabaseManager().getTop10KillPlayers().get(i).contains("Not available.")) {
                sorted10KillsPlayers.add(ChatColor.WHITE + "" + place + ". - " + mColor + game.getDatabaseManager().getTop10KillPlayers().get(i) + ": " +
                        sColor + game.getDatabaseManager().getKills(Bukkit.getOfflinePlayer(game.getDatabaseManager().getTop10KillPlayers().get(i))));
            } else {
                sorted10KillsPlayers.add(ChatColor.WHITE + "" + place + ". - " + mColor + game.getDatabaseManager().getTop10KillPlayers().get(i));
            }

            if(!game.getDatabaseManager().getTop10DeathPlayers().get(i).contains("Not available.")) {
                sorted10DeathPlayers.add(ChatColor.WHITE + "" + place + ". - " + mColor + game.getDatabaseManager().getTop10DeathPlayers().get(i) + ": " +
                        sColor + game.getDatabaseManager().getDeaths(Bukkit.getOfflinePlayer(game.getDatabaseManager().getTop10DeathPlayers().get(i))));
            } else {
                sorted10DeathPlayers.add(ChatColor.WHITE + "" + place + ". - " + mColor + game.getDatabaseManager().getTop10DeathPlayers().get(i));
            }

            if(!game.getDatabaseManager().getTop10WinPlayers().get(i).contains("Not available.")) {
                sorted10WinPlayers.add(ChatColor.WHITE + "" + place + ". - " + mColor + game.getDatabaseManager().getTop10WinPlayers().get(i) + ": " +
                        sColor + game.getDatabaseManager().getWins(Bukkit.getOfflinePlayer(game.getDatabaseManager().getTop10WinPlayers().get(i))));
            } else {
                sorted10WinPlayers.add(ChatColor.WHITE + "" + place + ". - " + mColor + game.getDatabaseManager().getTop10WinPlayers().get(i));
            }
        }

        inventory.setItem(11, new ItemHandler(Material.IRON_SWORD).setDisplayName(mColor + "Top 10 Kills").setLore(ChatColor.GRAY + "§m----------------------",
                sorted10KillsPlayers.get(0), sorted10KillsPlayers.get(1), sorted10KillsPlayers.get(2),
                sorted10KillsPlayers.get(3), sorted10KillsPlayers.get(4), sorted10KillsPlayers.get(5),
                sorted10KillsPlayers.get(6), sorted10KillsPlayers.get(7), sorted10KillsPlayers.get(8),
                sorted10KillsPlayers.get(9), ChatColor.GRAY + "§m----------------------").build());

        inventory.setItem(15, new ItemHandler(Material.FIREBALL).setDisplayName(mColor + "Top 10 Deaths").setLore(ChatColor.GRAY + "§m----------------------",
                sorted10DeathPlayers.get(0), sorted10DeathPlayers.get(1), sorted10DeathPlayers.get(2),
                sorted10DeathPlayers.get(3), sorted10DeathPlayers.get(4), sorted10DeathPlayers.get(5),
                sorted10DeathPlayers.get(6), sorted10DeathPlayers.get(7), sorted10DeathPlayers.get(8),
                sorted10DeathPlayers.get(9), ChatColor.GRAY + "§m----------------------").build());

        inventory.setItem(31, new ItemHandler(Material.NETHER_STAR).setDisplayName(mColor + "Top 10 Wins").setLore(ChatColor.GRAY + "§m----------------------",
                sorted10WinPlayers.get(0), sorted10WinPlayers.get(1), sorted10WinPlayers.get(2),
                sorted10WinPlayers.get(3), sorted10WinPlayers.get(4), sorted10WinPlayers.get(5),
                sorted10WinPlayers.get(6), sorted10WinPlayers.get(7), sorted10WinPlayers.get(8),
                sorted10WinPlayers.get(9), ChatColor.GRAY + "§m----------------------").build());

        inventory.setItem(44, new ItemHandler(Material.PAPER).setDisplayName(sColor + "Leaderboards update after the game has finished.").build());

        this.fillEmptySlots(inventory);
    }

    public void handleLeaderboardsInventory(Player player) {
        player.openInventory(leaderboardsInventory);
    }

    public void handleDisqualifyInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*5, mColor + "Disqualify a player.");

        for (UUID allPlayers : game.getPlayers()) {
            if(!Bukkit.getOfflinePlayer(allPlayers).isOnline()) {
                ItemStack playerStack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                SkullMeta playersMeta = (SkullMeta) playerStack.getItemMeta();
                playersMeta.setOwner(Bukkit.getOfflinePlayer(allPlayers).getName());
                playersMeta.setDisplayName(Bukkit.getOfflinePlayer(allPlayers).getName());
                playerStack.setItemMeta(playersMeta);
                inventory.addItem(playerStack);
            }
        }

        player.openInventory(inventory);
    }

    public void handleLobbyInventory(Player player) {
        Inventory inventory = player.getInventory();

        inventory.clear();

        inventory.setItem(2, new ItemHandler(Material.BOOK).setDisplayName(mColor + "Game Configuration").build());
        inventory.setItem(4, new ItemHandler(Material.BEACON).setDisplayName(mColor + "Your Statistics").build());
        inventory.setItem(6, new ItemHandler(Material.PAPER).setDisplayName(mColor + "Game Scenarios").build());
    }

}
