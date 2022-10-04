package fr.neyuux.refont.lg.event;

import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinGameEvent extends Event {
    final PlayerLG playerLG;


    public PlayerJoinGameEvent(PlayerLG playerLG) {
        this.playerLG = playerLG;
    }

    public PlayerLG getPlayerLG() {
        return playerLG;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
