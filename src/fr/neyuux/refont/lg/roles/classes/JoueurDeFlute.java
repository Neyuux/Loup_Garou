package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.List;

public class JoueurDeFlute extends Role {

    @Override
    public String getDisplayName() {
        return "§5§lJoueur de Flûte";
    }

    @Override
    public String getConfigName() {
        return "Joueur de Flute";
    }

    @Override
    public String getDeterminingName() {
        return null;
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est de gagner la partie (wow). Vous pouvez remporter celle-ci en §9enchantant tous les joueurs§f avec votre flûte. Chaque nuit, vous pouvez enchanter jusqu'à 2 personnes.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 30;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §5" + this.getTimeout()+ " secondes §fpour choisir qui vous voulez enchanter.";
    }

    
}
