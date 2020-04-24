package net.saikatsune.uhc.commands.editor;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.Scenarios;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ScenariosEditorCommand implements CommandExecutor, Listener {

    private Game game = Game.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("scenarioseditor")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.openInventory(Scenarios.toToggle());
            }
        }
        return false;
    }

    @EventHandler
    public void handleInventoryClickEvent(InventoryClickEvent event) {
        if(event.getClickedInventory() != null) {
            if(event.getClickedInventory().getName().equalsIgnoreCase("Scenarios Editor")) {
                if(event.getCurrentItem() != null) {
                    for (Scenarios scenarios : Scenarios.values()) {
                        if(event.getCurrentItem().getType() == scenarios.getScenarioItem().getType()) {
                            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(
                                    ChatColor.AQUA + scenarios.name())) {
                                event.setCancelled(true);

                                if(scenarios.isEnabled()) {
                                    scenarios.setEnabled(false);

                                    game.getScenariosInList().remove(ChatColor.WHITE + scenarios.name());
                                } else {
                                    scenarios.setEnabled(true);

                                    game.getScenariosInList().add(ChatColor.WHITE + scenarios.name());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
