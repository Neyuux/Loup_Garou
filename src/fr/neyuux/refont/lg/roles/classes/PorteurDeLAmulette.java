package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class PorteurDeLAmulette extends Role {

    public PorteurDeLAmulette() {
        super(
                "§6§lPorteur de §d§lL'Amulette",
                "Porteur de L'Amulette",
                "§fVous êtes §6§lPorteur de §d§lL'Amulette§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous ne pouvez pas mourir des Loups.",
                RoleEnum.PORTEUR_DE_L_AMULETTE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
