package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

public class IdiotDuVillage extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lIdiot §e§ldu Village";
    }

    @Override
    public String getConfigName() {
        return "Idiot du Village";
    }

    @Override
    public String getDeterminingName() {
        return "de l'" + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, une fois dans la partie, le village décide de vous pendre, ils §9reconnaîtront votre bêtise§f. Vous ne mourrez donc pas, mais §9vous ne pourrez plus voter§f.";
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


    //TODO onFinishVote
}
