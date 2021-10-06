package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Voyante extends Role {

    public Voyante() {
        super("§d§lVoyante",
                "§d§lVoyante",
                "Voyante",
                "§fVous êtes §d§lVoyante§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez §9apprendre le rôle d'un joueur§f.",
                RoleEnum.VOYANTE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
