package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class GrandMechantLoup extends Role {

    @Override
    public String getDisplayName() {
        return "�4�lGrand M�chant �c�lLoup";
    }

    @Override
    public String getConfigName() {
        return "Grand M�chant Loup";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer. Mais apr�s cela, vous vous r�veillez une deuxi�me fois pour �9d�vorer une deuxi�me personne�f...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.LOUP_GAROU;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �4" + this.getDisplayName() + " secondes�f pour d�vorer une deuxi�me personne.";
    }
}
