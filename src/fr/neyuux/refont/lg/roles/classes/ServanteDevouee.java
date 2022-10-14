package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class ServanteDevouee extends Role {

    @Override
    public String getDisplayName() {
        return "�d�lServante D�vou�e";
    }

    @Override
    public String getConfigName() {
        return "Servante D�vou�e";
    }

    @Override
    public String getDeterminingName() {
        return "de la " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, vous n'avez pas r�ellement d'objectif. Pendant la partie : lorsque quelqu'un mourra au vote, vous aurez 10 secondes pour choisir de �9prendre son r�le�f ou non. (Vous deviendrez donc le r�le que ce joueur incarnait)";
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
        return "�fVous avez �d" + this.getTimeout() + " secondes�f pour choisir de prendre le r�le du joueur mort.";
    }
}
