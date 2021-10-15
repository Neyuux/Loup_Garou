package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class President extends Role {

    public President() {
        super(
                "�e�lPr�sident",
                "Pr�sident",
                "�fVous �tes �e�lPr�sident�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Tous le monde connait votre identit�, mais si vous mourrez, le �9village a perdu�f. Vous poss�dez �galement le r�le de maire s'il est activ�.",
                RoleEnum.PRESIDENT,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
