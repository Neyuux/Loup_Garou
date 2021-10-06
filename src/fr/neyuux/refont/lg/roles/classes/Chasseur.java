package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Chasseur extends Role {

    public Chasseur() {
        super("�2�lChasseur",
                "�2�lChasseur",
                "Chasseur",
                "�fVous �tes �2�lChasseur�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). A votre mort, vous d�gainerez votre fusil et avec la derni�re balle de votre chargeur, vous pourrez �9emmener quelqu'un dans la mort avec vous�f.",
                RoleEnum.CHASSEUR,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
