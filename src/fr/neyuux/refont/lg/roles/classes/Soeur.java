package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Soeur extends Role {

    public Soeur() {
        super("�d�lSoeur",
                "�d�lSoeur",
                "Soeur",
                "�fVous �tes �d�lSoeur�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Pendant la partie, votre soeur sera votre co�quipi�re, vous pouvez donc �9lui faire confiance�f.",
                RoleEnum.SOEUR,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
