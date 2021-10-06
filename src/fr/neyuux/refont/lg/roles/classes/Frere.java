package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Frere extends Role {

    public Frere() {
        super("�3�lFr�re",
                "�3�lFr�re",
                "Frere",
                "�fVous �tes �3�lFr�re�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Pendant la partie, vos deux fr�res seront vos co�quipiers, vous pouvez donc �9leur faire confiance�f.",
                RoleEnum.FRERE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
