package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Pyromane extends Role {

    public Pyromane() {
        super("�6�lPyromane",
                "�6�lPyromane",
                "Pyromane",
                "�fVous �tes �6�lPyromane�f, votre but est d'�liminer tous les joueurs de la partie et de, par cons�quent, de gagner seul. Chaque nuit, vous pourrez d�cider d'enrober un joueur d'essence ou de mettre le feu � tous les joueurs d�j� huil�s...",
                RoleEnum.PYROMANE,
                Camps.AUTRE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
