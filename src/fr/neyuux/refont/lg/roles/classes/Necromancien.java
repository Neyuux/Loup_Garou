package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Necromancien extends Role {

    public Necromancien() {
        super("�9�lN�cromancien",
                "�9�lN�cromancien",
                "N�cromancien",
                "�fVous �tes �9�lN�gromancien�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez �9r�cussiter�f un joueur. S'il avait un pouvoir, il le perd et devient �e�lSimple �a�lVillageois�f.",
                RoleEnum.NECROMANCIEN,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
