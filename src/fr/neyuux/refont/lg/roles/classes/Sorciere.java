package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Sorciere extends Role {

    @Override
    public String getDisplayName() {
        return "�5�lSorci�re";
    }

    @Override
    public String getConfigName() {
        return "Sorciere";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Vous poss�dez 2 �2potions�f : une �apotion de vie�f�o(qui vous permettera de r�ssuciter un joueur, dont le nom vous sera donn�, d�vor� par les Loups)�f et une �4potion de mort�f�o(qui vous permettera de tuer un joueur de votre choix)�f.";
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
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �5" + this.getTimeout() + " secondes�f pour utiliser vos potions.";
    }
}
