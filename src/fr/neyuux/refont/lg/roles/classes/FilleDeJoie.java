package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class FilleDeJoie extends Role {

    public FilleDeJoie() {
        super("§d§lFille de Joie",
                "§d§lFille de Joie",
                "Fille de Joie",
                "§fVous êtes §d§lFille de Joie§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez aller ken un joueur. Si ce joueur est un Loup ou est mangé par les Loups, vous §9mourrez§f. Si les Loups essaient de vous tuer pendant que vous êtes chez quelqu'un d'autre, vous §9survivez§f.",
                RoleEnum.FILLE_DE_JOIE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
