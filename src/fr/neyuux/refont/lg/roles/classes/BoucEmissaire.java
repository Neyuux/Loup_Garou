package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class BoucEmissaire extends Role {

    public BoucEmissaire() {
        super("�c�lBouc �a�l�missaire",
                "�c�lBouc �a�l�missaire",
                "Bouc �missaire",
                "�fVous �tes �c�lBouc �a�l�missaire�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, pendant la partie, il y a �galit� dans les votes, �9vous mourrez�f. (�8�l�n�m�oGROSSE VICTIME�f)",
                RoleEnum.BOUC_EMISSAIRE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
