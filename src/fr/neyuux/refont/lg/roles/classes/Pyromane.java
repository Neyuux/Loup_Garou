package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Pyromane extends Role {

    public Pyromane() {
        super("§6§lPyromane",
                "§6§lPyromane",
                "Pyromane",
                "§fVous êtes §6§lPyromane§f, votre but est d'éliminer tous les joueurs de la partie et de, par conséquent, de gagner seul. Chaque nuit, vous pourrez décider d'enrober un joueur d'essence ou de mettre le feu à tous les joueurs déjà huilés...",
                RoleEnum.PYROMANE,
                Camps.AUTRE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
