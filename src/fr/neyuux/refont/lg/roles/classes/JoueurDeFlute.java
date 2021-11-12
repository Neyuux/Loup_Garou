package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class JoueurDeFlute extends Role {

    public JoueurDeFlute(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�5�lJoueur de Fl�te";
    }

    @Override
    public String getConfigName() {
        return "Joueur de Flute";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre objectif est de gagner la partie (wow). Vous pouvez remporter celle-ci en �9enchantant tous les joueurs�f avec votre fl�te. Chaque nuit, vous pouvez enchanter jusqu'� 2 personnes.";
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
        return "�fVous avez �5" + this.getTimeout()+ " secondes �fpour choisir qui vous voulez enchanter.";
    }
}
