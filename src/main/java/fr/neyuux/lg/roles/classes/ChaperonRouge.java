package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;

public class ChaperonRouge extends Role {

    @Override
    public String getDisplayName() {
        return "�b�lChaperon �c�lRouge";
    }

    @Override
    public String getConfigName() {
        return "Chaperon Rouge";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Tant que qu'un chasseur sera encore pr�sent dans la partie, vous ne pourrez �9pas mourrir�f.";
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
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }

    
}
