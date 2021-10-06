package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Jumeau extends Role {

    public Jumeau() {
        super("�5�lJumeau",
                "�5�lJumeau",
                "Jumeau",
                "�fVous �tes �5�lJumeau�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lors de la premi�re nuit, vous devrez choisir un joueur. Lorsque ce joueur mourra, vous obtiendrez son r�le.",
                RoleEnum.JUMEAU,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
