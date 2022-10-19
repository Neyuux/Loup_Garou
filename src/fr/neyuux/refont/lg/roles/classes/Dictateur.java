package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.List;

public class Dictateur extends Role {

    @Override
    public String getDisplayName() {
        return "�4�lDicta�2�lteur";
    }

    @Override
    public String getConfigName() {
        return "Dictateur";
    }

    @Override
    public String getDeterminingName() {
        return null;
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez faire un �9coup d'�tat �fet prendre le contr�le du vote. Vous serez le seul � pouvoir voter ; si vous tuez un membre du village, vous mourrez, sinon, vous obtiendrez le r�le de �bMaire�f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WOLFY;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �2" + this.getTimeout() + " secondes pour choisir de faire un coup d'�tat.";
    }

    
}
