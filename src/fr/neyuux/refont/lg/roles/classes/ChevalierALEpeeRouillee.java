package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class ChevalierALEpeeRouillee extends Role {

    public ChevalierALEpeeRouillee() {
        super("�7�lChevalier �e� l'�7�p�e �6�lRouill�e",
                "�7�lChevalier �e� l'�7�p�e �6�lRouill�e",
                "Chevalier a l'�p�e Rouill�e",
                "�fVous �tes �7�lChevalier �e� l'�7�p�e �6�lRouill�e�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Si vous mourrez par les loups, le premier Loup � votre gauche�o(r�union)�f ou en dessous dans le tab�o(libre)�f tombera gravement malade : �9il mourra�f au tour suivant.",
                RoleEnum.CHEVALIER_A_L_EPEE_ROUILLEE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
