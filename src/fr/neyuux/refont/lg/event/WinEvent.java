package fr.neyuux.refont.lg.event;

import fr.neyuux.refont.lg.WinCamps;
import fr.neyuux.refont.lg.roles.Camps;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WinEvent extends Event implements Cancellable {

    public WinEvent(WinCamps camp) {
        this.camp = camp;
    }

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    private WinCamps camp;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    public WinCamps getCamp() {
        return camp;
    }

    public void setCamp(WinCamps camp) {
        this.camp = camp;
    }
}
