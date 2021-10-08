package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Pacifiste extends Role {

    public Pacifiste() {
        super("§d§lPacifiste",
                "§d§lPacifiste",
                "Pacifiste",
                "§fVous êtes §dPacifiste§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez révéler le rôle d'un joueur et empêcher tous les joueurs de voter ce jour là.",
                RoleEnum.PACIFISTE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
