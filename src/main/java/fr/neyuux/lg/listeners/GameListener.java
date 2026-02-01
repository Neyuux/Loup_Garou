package fr.neyuux.lg.listeners;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.DayCycle;
import fr.neyuux.lg.enums.GameState;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.WinEvent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class GameListener implements Listener {

    @EventHandler
    public void onCloseMaireInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (inv.getName().equals("§eDépartager les Votes") && (boolean) PlayerLG.createPlayerLG(player).getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    
                    player.openInventory(inv);
                    
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent ev) {
        Player player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (playerLG.isSleeping()) {
            Location loc = playerLG.getPlacement();

            BlockPosition bp = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutBed packet = new PacketPlayOutBed(ePlayer, bp);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ePlayer.playerConnection.sendPacket(packet);
                }
            }.runTaskLater(LG.getInstance(), 3L);
        }
    }

    @EventHandler
    public void onMoove(PlayerMoveEvent ev) {
        GameLG game = LG.getInstance().getGame();
        if (!game.getGameType().equals(GameType.MEETING)) return;
        if (!game.getGameState().equals(GameState.PLAYING)) return;
        if (game.getDayCycle().equals(DayCycle.NONE)) return;

        Location from = ev.getFrom();

        if (PlayerLG.createPlayerLG(ev.getPlayer()).isDead()) return;

        if (from.distanceSquared(ev.getTo()) > 0.001D)
            ev.setTo(from);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent ev) {
        GameLG game = LG.getInstance().getGame();
        Player player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (game.getGameState().equals(GameState.PLAYING)) {
            game.getSpectators().remove(playerLG);

            if (game.getAlive().contains(playerLG)) {
                playerLG.eliminate();
                Bukkit.broadcastMessage(LG.getPrefix() + "§c" + player.getName() + " a été éliminé car il s'est déconnecté.");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        GameLG game = LG.getInstance().getGame();
        Player player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (game.getGameState().equals(GameState.PLAYING)) {
            player.setGameMode(GameMode.SPECTATOR);

            player.resetMaxHealth();
            player.setHealth(player.getMaxHealth());

            player.sendMessage(LG.getPrefix() + "§9Le jeu a déjà démarré !");
            player.sendMessage(LG.getPrefix() + "§9Votre mode de jeu a été établi en §7spectateur§9.");
            game.getSpectators().add(playerLG);
        }
    }

    @EventHandler
    public void onClick(PlayerAnimationEvent ev) {
        Player player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (ev.getAnimationType().equals(PlayerAnimationType.ARM_SWING) && playerLG.isChoosing())
            playerLG.callbackChoice(playerLG.getPlayerOnCursor(LG.getInstance().getGame().getAlive()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWinEvent(WinEvent ev) {
        if (!ev.isCancelled()) {
            GameLG game = LG.getInstance().getGame();
            game.win(ev.getCamp(), game.getAlive());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent ev) {
        GameLG game = LG.getInstance().getGame();
        PlayerLG playerLG = PlayerLG.createPlayerLG(ev.getPlayer());
        String displayName = ev.getPlayer().getDisplayName();
        String msg = ev.getMessage().trim();

        if (!game.getGameState().equals(GameState.PLAYING)) return;

        if (playerLG.isSpectator()) {

            ev.setCancelled(true);
            game.getSpectators().forEach(specLG -> specLG.sendMessage(displayName + " §7§l» §7" + msg));

        } else {

            if (game.getDayCycle().equals(DayCycle.DAY)) {
                ev.setFormat("§e" + displayName + " §8§l» §f" + msg);
            } else {
                ev.setCancelled(true);
            }
        }
    }

}
