package fr.neyuux.refont.lg.listeners;

import fr.neyuux.refont.lg.PlayerLG;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class NightListener implements Listener {

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent ev) {
        Player player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (playerLG.isSleeping()) {
            Location loc = playerLG.getPlacement();

            player.teleport(loc);
            ((CraftPlayer) player).getHandle().a(new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
            player.setSleepingIgnored(false);
        }

    }

}
