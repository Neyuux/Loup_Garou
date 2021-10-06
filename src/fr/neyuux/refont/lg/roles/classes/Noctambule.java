package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Noctambule extends Role {

    public Noctambule() {
        super("§9§lNoctambule",
                "§9§lNoctambule",
                "Noctambule",
                "§fVous êtes §9§lNoctambule§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous devez choisir un joueur chez qui §9dormir§f, ce joueur connaîtra alors votre identité mais est privé de ses pouvoirs pour la nuit.",
                RoleEnum.NOCTAMBULE,
                Camps.VILLAGE,
                Decks.ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
