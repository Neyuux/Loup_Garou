package fr.neyuux.refont.lg.event;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.VoteLG;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteEndEvent extends Event {

    private final PlayerLG choosen;
    private final VoteLG vote;
    private boolean cancelled = false;

    public VoteEndEvent(VoteLG vote, PlayerLG choosenLG) {
        this.vote = vote;
        this.choosen = choosenLG;
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

    public VoteLG getVote() {
        return vote;
    }
}
