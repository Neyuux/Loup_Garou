package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class GrandMechantLoup extends Role {

    public GrandMechantLoup() {
        super("§4§lGrand Méchant §c§lLoup",
                "§4§lGrand Méchant §c§lLoup",
                "Grand Méchant Loup",
                "§fVous êtes §4§lGrand Méchant §c§lLoup§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Mais après cela, vous vous réveillez une deuxième fois pour §9dévorer une deuxième personne§f...",
                RoleEnum.GRAND_MECHANT_LOUP,
                Camps.LOUP_GAROU,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
