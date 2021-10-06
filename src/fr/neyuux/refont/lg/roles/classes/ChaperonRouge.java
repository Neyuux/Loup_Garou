package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class ChaperonRouge extends Role {

    public ChaperonRouge() {
        super("�b�lChaperon �c�lRouge",
                "�b�lChaperon �c�lRouge",
                "Chaperon Rouge",
                "�fVous �tes �b�lChaperon �c�lRouge�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Tant que qu'un chasseur sera encore pr�sent dans la partie, les Loups ne �9pourront pas vous tuer�f.",
                RoleEnum.CHAPERON_ROUGE,
                Camps.VILLAGE,
                Decks.WOLFY);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
