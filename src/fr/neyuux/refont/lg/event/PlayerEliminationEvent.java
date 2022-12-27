package fr.neyuux.refont.lg.event;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.VoteLG;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEliminationEvent extends Event implements Cancellable {

    private final PlayerLG choosen;
    private boolean cancel;

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
