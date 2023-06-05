package fr.neyuux.lg.event;

import fr.neyuux.lg.VoteLG;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteStartEvent extends Event {

    private final VoteLG vote;

    public VoteStartEvent(VoteLG vote) {
        this.vote = vote;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public VoteLG getVote() {
        return vote;
    }
}
