package fr.neyuux.refont.lg.listeners;

import fr.neyuux.refont.lg.DayCycle;
import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import fr.neyuux.refont.old.lg.Gcycle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ConcurrentModificationException;

public class PreGameListener implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);


        if (!LG.getInstance().getGame().getGameState().equals(GameState.PLAYING)) {

            LG.setPlayerInScoreboardTeam("Players", player);
            if (player.isOp()) LG.getInstance().getGame().OP(playerLG);

            playerLG.showAllPlayers();
            LG.getInstance().getGame().addSaturation(playerLG);
            player.setGameMode(GameMode.ADVENTURE);
            //TODO TP
            LG.getInstance().getItemsManager().updateSpawnItems(playerLG);
            //TODO update scoreboard
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        LG.getInstance().getGame().getOPs().remove(player);

        if (!LG.getInstance().getGame().getGameState().equals(GameState.PLAYING)) {

            LG.getInstance().getGame().leaveGame(playerLG);
            //TODO updateScoreboard
        }

        if (PlayerLG.getPlayersMap().containsKey(player) && (LG.getInstance().getGame().getGameState().equals(GameState.WAITING) || LG.getInstance().getGame().getGameState().equals(GameState.STARTING))) {
            PlayerLG.removePlayerLG(player);
        }
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
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler()
    public void onDamage(EntityDamageEvent ev) {
        if (ev.getEntityType().equals(EntityType.PLAYER)) {

            if (!LG.getInstance().getGame().getDayCycle().equals(DayCycle.NIGHT) || ev.getDamage() != 0.1)
                ev.setCancelled(true);
            else ev.setDamage(0);

        } else if (ev.getEntityType().equals(EntityType.ITEM_FRAME)) ev.setCancelled(true);

        else if (ev.getEntityType().equals(EntityType.ARMOR_STAND)) {
            if (ev.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent ev) {
        if (ev.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) ev.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent ev) {
        Player player = ev.getPlayer();
        String cmd = ev.getMessage();

        if (cmd.toLowerCase().startsWith("/me") || cmd.toLowerCase().startsWith("/say") || cmd.toLowerCase().startsWith("/minecraft:")) {
            ev.setCancelled(true);
            player.sendMessage("§4Vous n'êtes pas autorisé à me péter les couilles.");
            return;
        }

        final String[] strings = cmd.toLowerCase().substring(1).split(" ", 2);
        if (Bukkit.getPluginCommand("tell").getAliases().contains(strings[0]) || cmd.toLowerCase().startsWith("/tell") || Bukkit.getPluginCommand("respond").getAliases().contains(strings[0]) || cmd.toLowerCase().startsWith("/respond")) {
            ev.setCancelled(true);
            player.sendMessage("§4§lNey§6G§ei§2n§4§l_ » §4Cette commande est désactivée en " + LG.getPrefix() + "§4.");
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent ev) {
        Player player = ev.getPlayer();
        String msg = ev.getMessage();

        if (LG.getInstance().getGame().getDayCycle().equals(DayCycle.NONE))
            ev.setFormat(player.getDisplayName() + "»" + msg);
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
