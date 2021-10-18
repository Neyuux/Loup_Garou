package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class IdiotDuVillage extends Role {

    public IdiotDuVillage(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�d�lIdiot �e�ldu Village";
    }

    @Override
    public String getScoreboardName() {
        return "�d�lIdiot �e�ldu Village";
    }

    @Override
    public String getConfigName() {
        return "Idiot du Village";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, une fois dans la partie, le village d�cide de vous pendre, ils �9reconna�tront votre b�tise�f. Vous ne mourrez donc pas, mais �9vous ne pourrez plus voter�f.";
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
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }
}
