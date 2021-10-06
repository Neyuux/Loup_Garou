package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class ServanteDevouee extends Role {

    public ServanteDevouee() {
        super("�d�lServante �5D�vou�e",
                "�d�lServante �5D�vou�e",
                "Servante D�vou�e",
                "�fVous �tes �d�lServante �5D�vou�e�f, vous n'avez pas r�ellement d'objectif. Pendant la partie : lorsque quelqu'un mourra au vote, vous aurez 10 secondes pour choisir de �9prendre son r�le�f ou non. (Vous deviendrez donc le r�le que ce joueur incarnait)",
                RoleEnum.SERVANTE_DEVOUEE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
