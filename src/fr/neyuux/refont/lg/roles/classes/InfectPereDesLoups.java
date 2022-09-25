package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class InfectPereDesLoups extends Role {

    @Override
    public String getDisplayName() {
        return "�2�lInfect P�re �c�ldes Loups";
    }

    @Override
    public String getConfigName() {
        return "Infect Pere des Loups";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer. Apr�s cela, vous vous r�veillez et �9choisissez si votre victime deviendra �c�lLoup-Garou�f (elle gardera �galement ses pouvoirs de villageois si elle en a un).";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �c" + this.getTimeout() + " secondes �fpour choisir d'infecter ou non le joueur cibl� cette nuit.";
    }
}
