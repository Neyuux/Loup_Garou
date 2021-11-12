package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class ServanteDevouee extends Role {

    public ServanteDevouee(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§d§lServante Dévouée";
    }

    @Override
    public String getConfigName() {
        return "Servante Dévouée";
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
}
