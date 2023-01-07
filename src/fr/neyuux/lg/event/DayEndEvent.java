package fr.neyuux.lg.event;

import fr.neyuux.lg.PlayerLG;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DayEndEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;
    private PlayerLG killedLG;

    public DayEndEvent(PlayerLG killedLG) {
        this.killedLG = killedLG;
    }

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

    public PlayerLG getKilledLG() {
        return killedLG;
    }

    public void setKilledLG(PlayerLG killedLG) {
        this.killedLG = killedLG;
    }
}
