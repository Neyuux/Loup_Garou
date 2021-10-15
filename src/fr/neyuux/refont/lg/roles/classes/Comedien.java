package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Comedien extends Role {

    public Comedien(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�5�lCom�dien";
    }

    @Override
    public String getScoreboardName() {
        return "�5�lCom�dien";
    }

    @Override
    public String getConfigName() {
        return "Com�dien";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Au d�but de la partie, vous obtiendrez �93 pouvoirs de r�les villageois�f que vous pourrez utiliser une fois chacun pendant la partie.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �d" + getTimeout() + " secondes �fpour choisir un r�le pour cette nuit.";
    }
}
