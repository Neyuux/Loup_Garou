package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class EnfantSauvage extends Role {

    @Override
    public String getDisplayName() {
        return "�6�lEnfant Sauvage";
    }

    @Override
    public String getConfigName() {
        return "Enfant Sauvage";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, vous allez, au d�but de la partie, devoir choisir votre ma�tre. Si celui-ci �9meurt�f, vous devenez un �c�lLoup-Garou�f. Tant que cela ne s'est pas pass�, votre but est d'�liminer est �9d'�liminer tous les loups-garous (ou r�les solos)�f.";
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
        return "�fVous avez �6" + this.getTimeout() + " secondes �fpour choisir votre mod�le.";
    }
}
