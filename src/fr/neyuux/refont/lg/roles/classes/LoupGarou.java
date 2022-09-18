package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class LoupGarou extends Role {

    @Override
    public String getDisplayName() {
        return "�c�lLoup-Garou";
    }

    @Override
    public String getConfigName() {
        return "Loup-Garou";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer...";
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
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �c" + this.getTimeout() + " secondes �fpour voter pour choisir qui d�vorer.";
    }

}
