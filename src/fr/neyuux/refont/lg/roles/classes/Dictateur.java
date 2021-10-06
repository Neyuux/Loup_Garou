package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Dictateur extends Role {

    public Dictateur() {
        super("�4�lDicta�2�lteur",
                "�4�lDicta�2�lteur",
                "Dictateur",
                "�fVous �tes �4�lDicta�2�lteur�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez faire un �9coup d'�tat �fet prendre le contr�le du vote. Vous serez le seul � pouvoir voter ; si vous tuez un membre du village, vous mourrez, sinon, vous obtiendrez le r�le de �bMaire�f.",
                RoleEnum.DICTATEUR,
                Camps.VILLAGE,
                Decks.WOLFY);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
