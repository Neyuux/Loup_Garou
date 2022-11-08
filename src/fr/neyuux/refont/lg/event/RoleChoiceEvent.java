package fr.neyuux.refont.lg.event;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RoleChoiceEvent extends Event implements Cancellable {

    private final PlayerLG choosen;
    private final Role role;
    private boolean cancelled = false;

    public RoleChoiceEvent(Role role, PlayerLG choosenLG) {
        this.role = role;
        this.choosen = choosenLG;
        System.out.println(role.getConfigName() + " choice -> " + choosenLG.getName());
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerLG getChoosen() {
        return choosen;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
