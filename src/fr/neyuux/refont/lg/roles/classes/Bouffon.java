package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Bouffon extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lBouffon";
    }

    @Override
    public String getConfigName() {
        return "Bouffon";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est de vous faire voter par le village. Si vous réussissez : vous aurez la possibilité de tuer un des joueurs qui a voté pour vous.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d " + this.getTimeout() + " secondes§f pour tuer un joueur.";
    }

}
