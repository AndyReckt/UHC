package net.saikatsune.uhc.listener;

import me.uhc.worldborder.Events.WorldBorderFillFinishedEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.populators.CanePopulator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockChangeListener implements Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    private final boolean populateSugarcane = game.getConfig().getBoolean("POPULATORS.SUGARCANE.ENABLED");

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(game.getSpectators().contains(player)) event.setCancelled(true);

        if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
            event.setCancelled(true);
        } else {
            if(game.getPlayers().contains(player.getUniqueId())) {
                if(event.getBlock().getType() == Material.DIAMOND_ORE) {
                    for (Player allPlayers : game.getSpectators()) {
                        if(allPlayers.hasPermission("uhc.host")) {
                           if (game.getReceiveDiamondAlerts().contains(allPlayers.getUniqueId())) {

                               TextComponent textComponent = new TextComponent();

                               textComponent.setText(prefix + mColor + player.getName() + sColor + " has mined diamonds!");

                               textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                       new ComponentBuilder(sColor + "Click to teleport to " + mColor + player.getName() + sColor + "!").create()));
                               textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));

                               allPlayers.spigot().sendMessage(textComponent);
                           }
                        }
                    }
                } else if(event.getBlock().getType() == Material.GOLD_ORE) {
                    for (Player allPlayers : game.getSpectators()) {
                        if(allPlayers.hasPermission("uhc.host")) {
                            if (game.getReceiveGoldAlerts().contains(allPlayers.getUniqueId())) {
                                TextComponent textComponent = new TextComponent();

                                textComponent.setText(prefix + mColor + player.getName() + sColor + " has mined gold!");

                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(sColor + "Click to teleport to " + mColor + player.getName() + sColor + "!").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));

                                allPlayers.spigot().sendMessage(textComponent);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(game.getSpectators().contains(player)) event.setCancelled(true);

        if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
            event.setCancelled(true);
        } else {
            if(game.isInGrace()) {
                switch (event.getBlockPlaced().getType()) {
                    case FIRE:
                        event.setCancelled(true);
                        player.sendMessage(prefix + ChatColor.RED + "You are not allowed to place or fire " +
                                "in grace period to prevent iPvP.");
                    break;
                }
            }
        }
    }

    @EventHandler
    public void handlePlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();

        Material bucket = event.getBucket();

        if(bucket.toString().contains("LAVA")) {
            if(game.isInGrace()) {
                event.setCancelled(true);

                player.sendMessage(prefix + ChatColor.RED + "You are not allowed to place lava or fire " +
                        "in grace period to prevent iPvP.");
            }
        }
    }

    @EventHandler
    public void handleChunkLoadFinishEvent(WorldBorderFillFinishedEvent event) {
        if(!event.getWorld().getName().equalsIgnoreCase("uhc_practice")) {
            Bukkit.broadcastMessage(prefix + sColor + "Finished loading the world " + mColor + event.getWorld().getName() + sColor + "!");
            Bukkit.broadcastMessage(prefix + ChatColor.RED + "Restarting server in 10 seconds!");

            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.shutdown();
                }
            }.runTaskLater(game, 10 * 20);
        }
    }

    @EventHandler
    public void handleChunkUnloadEvent(ChunkUnloadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleWorldInitiateEvent(WorldInitEvent event) {
        World world = event.getWorld();

        if(populateSugarcane) {
            world.getPopulators().add(new CanePopulator());
        }
    }
}
