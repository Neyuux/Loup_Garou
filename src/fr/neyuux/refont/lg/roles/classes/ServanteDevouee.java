package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class ServanteDevouee extends Role {

    public ServanteDevouee() {
        super("§d§lServante §5Dévouée",
                "§d§lServante §5Dévouée",
                "Servante Dévouée",
                "§fVous êtes §d§lServante §5Dévouée§f, vous n'avez pas réellement d'objectif. Pendant la partie : lorsque quelqu'un mourra au vote, vous aurez 10 secondes pour choisir de §9prendre son rôle§f ou non. (Vous deviendrez donc le rôle que ce joueur incarnait)",
                RoleEnum.SERVANTE_DEVOUEE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
