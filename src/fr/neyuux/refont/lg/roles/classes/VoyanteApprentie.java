package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class VoyanteApprentie extends Role {

    public VoyanteApprentie() {
        super(
                "�d�lVoyante �a�lApprentie",
                "Voyante Apprentie",
                "�fVous �tes �d�lVoyante �a�lApprentie�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lorsque qu'un joueur qui est voyante mourra, vous prendrez sa place et son r�le.",
                RoleEnum.VOYANTE_APPRENTIE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
