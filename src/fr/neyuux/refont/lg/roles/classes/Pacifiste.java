package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Pacifiste extends Role {

    public Pacifiste() {
        super("�d�lPacifiste",
                "�d�lPacifiste",
                "Pacifiste",
                "�fVous �tes �dPacifiste�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez r�v�ler le r�le d'un joueur et emp�cher tous les joueurs de voter ce jour l�.",
                RoleEnum.PACIFISTE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
