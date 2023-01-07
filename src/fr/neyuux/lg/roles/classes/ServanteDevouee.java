package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.event.VoteEndEvent;
import fr.neyuux.lg.inventories.roleinventories.ServanteDevoueeInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

public class ServanteDevouee extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lServante Dévouée";
    }

    @Override
    public String getConfigName() {
        return "Servante Dévouée";
    }

    @Override
    public String getDeterminingName() {
        return "de la " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, vous n'avez pas réellement d'objectif. Pendant la partie : lorsque quelqu'un mourra au vote, vous aurez 10 secondes pour choisir de §9prendre son rôle§f ou non. (Vous deviendrez donc le rôle que ce joueur incarnait)";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d" + this.getTimeout() + " secondes§f pour choisir de prendre le rôle du joueur mort.";
    }


    @EventHandler
    public void onVoteEnd(VoteEndEvent ev) {
        PlayerLG choosen = ev.getChoosen();
        PlayerLG servante;
        VoteLG vote = ev.getVote();
        GameLG game = LG.getInstance().getGame();

        if (game.getPlayersByRole(this.getClass()).isEmpty()) return;
        else servante = game.getPlayersByRole(this.getClass()).get(0);

        if (vote.getName().equals("Vote du Village") && choosen != null && !choosen.getRole().getClass().equals(this.getClass()) && servante.canUsePowers()) {
            vote.setCallback(() -> {
                new ServanteDevoueeInv(this, servante, choosen, vote.getCallback()).open(servante.getPlayer());
                game.wait(35, () -> {
                    servante.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps.");
                    vote.getCallback().run();
                }, ((param1LGPlayer, secondsLeft) -> LG.getPrefix() + "§fAu tour " + this.getDeterminingName()), true);
            });
        }
    }
}
