package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Sorciere extends Role {

    public Sorciere() {
        super("�5�lSorci�re",
                "�5�lSorci�re",
                "Sorciere",
                "�fVous �tes �5�lSorci�re�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Vous poss�dez 2 �2potions�f : une �apotion de vie�f�o(qui vous permettera de r�ssuciter un joueur, dont le nom vous sera donn�, d�vor� par les Loups)�f et une �4potion de mort�f�o(qui vous permettera de tuer un joueur de votre choix)�f.",
                RoleEnum.SORCIERE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
