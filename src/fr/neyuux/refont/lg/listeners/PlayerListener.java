package fr.neyuux.refont.lg.listeners;

import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final LG main;


    public PlayerListener(LG main) {
        this.main = main;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        //TODO
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (PlayerLG.getPlayersMap().containsKey(player) && (LG.getInstance().getGame().getGameState().equals(GameState.WAITING) || LG.getInstance().getGame().getGameState().equals(GameState.STARTING))) {
            PlayerLG.removePlayerLG(player);
        }
    }

}
