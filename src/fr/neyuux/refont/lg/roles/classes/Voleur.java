package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Voleur extends Role {

    public Voleur(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�3�lVoleur";
    }

    @Override
    public String getConfigName() {
        return "Voleur";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, au d�but de la partie, vous allez devoir �9choisir �fentre les deux r�les qui n'ont pas �t� distribu� (ou en choisir aucun)...";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �3" + this.getTimeout() + " secondes�f pour choisir votre r�le.";
    }
}
