package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Comedien extends Role {

    public Comedien() {
        super("�5�lCom�dien",
                "�5�lCom�dien",
                "Com�dien",
                "�fVous �tes �5�lCom�dien�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Au d�but de la partie, vous obtiendrez �93 pouvoirs de r�les villageois�f que vous pourrez utiliser une fois chacun pendant la partie.",
                RoleEnum.COMEDIEN,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
