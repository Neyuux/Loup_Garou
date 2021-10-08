package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class VoyanteDAura extends Role {

    public VoyanteDAura() {
        super("§d§lVoyante §4§ld'Aura",
                "§d§lVoyante §4§ld'Aura",
                "Voyante d'Aura",
                "§fVous êtes §d§lVoyante §4§ld'Auraf, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous vous réveillerez et découvrez si un joueur que vous choisirez est Loup ou non.",
                RoleEnum.VOYANTE_APPRENTIE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
