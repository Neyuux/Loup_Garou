package fr.neyuux.refont.lg.listeners;

import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ConcurrentModificationException;

public class PlayerListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (player.isOp()) LG.getInstance().getGame().OP(playerLG);
        //TODO
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (PlayerLG.getPlayersMap().containsKey(player) && (LG.getInstance().getGame().getGameState().equals(GameState.WAITING) || LG.getInstance().getGame().getGameState().equals(GameState.STARTING))) {
            PlayerLG.removePlayerLG(player);
        }

        LG.getInstance().getGame().getOPs().remove(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) return;

        try {
            for (CustomItemStack customitem : CustomItemStack.getItemList())
                if (customitem.isCustomSimilar(item)) {
                    event.setCancelled(true);
                    customitem.use(player, event);
                }
        } catch (ConcurrentModificationException ignored) {

        }
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null) return;

        try {
            for (CustomItemStack customitem : CustomItemStack.getItemList())
                if (customitem.isCustomSimilar(item)) {
                    event.setCancelled(true);
                    customitem.use(human, event);
                }
        } catch (ConcurrentModificationException ignored) {}
    }

}
