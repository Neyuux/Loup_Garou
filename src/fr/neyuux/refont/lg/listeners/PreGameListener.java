package fr.neyuux.refont.lg.listeners;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.ResetEvent;
import fr.neyuux.refont.lg.event.TryStartGameEvent;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.roles.classes.*;
import fr.neyuux.refont.lg.tasks.LGStart;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import fr.neyuux.refont.old.lg.Gcycle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class PreGameListener implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        LG lg = LG.getInstance();
        GameLG game = lg.getGame();


        if (!game.getGameState().equals(GameState.PLAYING)) {

            LG.setPlayerInScoreboardTeam("Players", player);
            if (player.isOp()) LG.getInstance().getGame().OP(playerLG);

            playerLG.showAllPlayers();
            game.addSaturation(playerLG);
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(Bukkit.getWorld("LG"), new Random().nextInt(16) + 120, 16.5, new Random().nextInt(16) + 371));
            lg.getItemsManager().updateSpawnItems(playerLG);
            game.sendLobbySideScoreboardToAllPlayers();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        GameLG game = LG.getInstance().getGame();

        game.getOPs().remove(player);

        if (!game.getGameState().equals(GameState.PLAYING)) {

            game.leaveGame(playerLG);
            game.sendLobbySideScoreboardToAllPlayers();
        }

        if (PlayerLG.getPlayersMap().containsKey(player) && (game.getGameState().equals(GameState.WAITING) || game.getGameState().equals(GameState.STARTING))) {
            PlayerLG.removePlayerLG(player);
        }
    }

    @EventHandler
    public void onReset(ResetEvent ev) {
        LoupGarou.CHAT.closeChat();
        Cupidon.CHAT.closeChat();
        Soeur.CHAT.closeChat();
        Frere.CHAT.closeChat();
        Bouffon.NEED_TO_PLAY.clear();
        Chasseur.NEED_TO_PLAY.clear();
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
            ev.setFormat(player.getDisplayName() + " §7» §f" + msg);
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

    @EventHandler
    public void onJoinGame(TryStartGameEvent ev) {
        GameLG game = LG.getInstance().getGame();

        if (game.getPlayersInGame().size() == Bukkit.getOnlinePlayers().size() - game.getSpectators().size() && game.getPlayersInGame().size() > 0 && game.getGameState().equals(GameState.WAITING)) {
            if (game.getConfig().getAddedRoles().size() >= game.getPlayersInGame().size()) {
                LG.getInstance().getGame().setGameState(GameState.STARTING);
                new LGStart(game).runTaskTimer(LG.getInstance(), 0, 20);
            }
        }
    }

    @EventHandler
    public void onBreakEmplacement(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        Player player = ev.getPlayer();

        if (player.getItemInHand().getType().equals(Material.STONE_HOE) && player.isOp()) {
            ev.setCancelled(true);
            if (LG.getInstance().getGame().getGameType() != GameType.NONE) {
                Location loc = block.getLocation();
                List<Object> list = (List<Object>) LG.getInstance().getConfig().getList("spawns." + LG.getInstance().getGame().getGameType());

                list.add(Arrays.asList((double) loc.getBlockX() + 0.5D, loc.getY(), (double) loc.getBlockZ() + 0.5D, (double) loc.getYaw(), (double) loc.getPitch()));

                LG.getInstance().saveConfig();
                player.sendMessage(LG.getPrefix() + "§aLa position a bien été ajoutée ! §e§o(" + block.getType() + ")");
            } else {
                player.sendMessage(LG.getPrefix() + "§cVous devez choisir le type de jeu avant de créer des emplacements !");
            }
        }
    }

}
