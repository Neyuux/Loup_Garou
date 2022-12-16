package fr.neyuux.refont.lg.listeners;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.old.lg.Gcycle;
import fr.neyuux.refont.old.lg.Gstate;
import fr.neyuux.refont.old.lg.Gtype;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class NightListener implements Listener {

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent ev) {
        Player player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (playerLG.isSleeping()) {
            Location loc = playerLG.getPlacement();

            BlockPosition bp = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutBed packet = new PacketPlayOutBed(ePlayer, bp);

            ePlayer.playerConnection.sendPacket(packet);
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

}
