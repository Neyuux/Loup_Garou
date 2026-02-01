package fr.neyuux.lg.event;

import fr.neyuux.lg.PlayerLG;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class PlayerEliminationEvent extends Event implements Cancellable {

    private final PlayerLG choosen;
    private boolean cancel;
    @Getter
    private final HashMap<PlayerLG, String> messagesToSend = new HashMap<>();

    public PlayerEliminationEvent(PlayerLG choosenLG) {
        this.choosen = choosenLG;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerLG getEliminated() {
        return choosen;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
