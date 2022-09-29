package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class ChevalierALEpeeRouillee extends Role {

    @Override
    public String getDisplayName() {
        return "�7�lChevalier �e� l'�7�p�e �6�lRouill�e";
    }

    @Override
    public String getConfigName() {
        return "Chevalier a l'�p�e Rouill�e";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Si vous mourrez par les loups, le premier Loup � votre gauche�o(r�union)�f ou en dessous dans le tab�o(libre)�f tombera gravement malade : �9il mourra�f au tour suivant.";
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
