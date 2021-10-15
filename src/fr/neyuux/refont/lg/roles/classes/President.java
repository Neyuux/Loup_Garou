package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class President extends Role {

    public President() {
        super(
                "§e§lPrésident",
                "Président",
                "§fVous êtes §e§lPrésident§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Tous le monde connait votre identité, mais si vous mourrez, le §9village a perdu§f. Vous possédez également le rôle de maire s'il est activé.",
                RoleEnum.PRESIDENT,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
